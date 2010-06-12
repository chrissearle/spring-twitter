/*
 * Copyright 2010 Chris Searle
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.chrissearle.spring.twitter.spring;

import net.chrissearle.spring.twitter.service.FollowService;
import net.chrissearle.spring.twitter.service.TwitterServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class Twitter4jFollowService extends AbstractTwitter4JSupport implements FollowService {
    private final Logger logger = Logger.getLogger(Twitter4jFollowService.class.getName());


    @Autowired
    public Twitter4jFollowService(Twitter twitter) {
        super(twitter);
    }

    @Override
    public void follow(String twitterUserId) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(new StringBuilder().append("Following: ").append(twitterUserId).toString());
        }

        if (getTwitterActiveFlag()) {
            addTwitterFriendshipAndNotification(twitterUserId);
        } else {
            if (logger.isLoggable(Level.INFO)) {
                logger.info("Twitter disabled");
            }
        }
    }

    @Override
    public boolean isFriend(String twitterUserId) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(new StringBuilder().append("Checking isFriend: ").append(twitterUserId).toString());
        }

        if (getTwitterActiveFlag()) {
            return checkForFriendship(twitterUserId);
        } else {
            if (logger.isLoggable(Level.INFO)) {
                logger.info("Twitter disabled");
            }

            return false;
        }
    }

    @Override
    public void unfollow(String twitterUserId) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info(new StringBuilder().append("Stopping following: ").append(twitterUserId).toString());
        }

        if (getTwitterActiveFlag()) {
            removeTwitterFriendshipAndNotification(twitterUserId);
        } else {
            if (logger.isLoggable(Level.INFO)) {
                logger.info("Twitter disabled");
            }
        }
    }


    private boolean checkForFriendship(String twitterId) {
        try {
            return alreadyFriends(twitterId);
        } catch (TwitterException e) {
            final String message = new StringBuilder().append("Unable to check isFriend ").append(twitterId).append(" due to ").append(e.getMessage()).toString();

            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(message);
            }

            throw new TwitterServiceException(message, e);
        }
    }

    private void addTwitterFriendshipAndNotification(String twitterId) {
        try {
            establishFriendshipWithNotification(twitterId);
        } catch (TwitterException e) {
            final String message = new StringBuilder().append("Unable to follow ").append(twitterId).append(" due to ").append(e.getMessage()).toString();

            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(message);
            }

            throw new TwitterServiceException(message, e);
        }
    }

    private void removeTwitterFriendshipAndNotification(String twitterId) {
        try {
            removeFriendshipWithNotification(twitterId);
        } catch (TwitterException e) {
            final String message = new StringBuilder().append("Unable to stop following ").append(twitterId).append(" due to ").append(e.getMessage()).toString();

            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(message);
            }

            throw new TwitterServiceException(message, e);
        }
    }

    private void establishFriendshipWithNotification(String twitterId) throws TwitterException {
        if (!alreadyFriends(twitterId)) {
            twitter.createFriendship(twitterId);
            twitter.enableNotification(twitterId);
        }
    }

    private void removeFriendshipWithNotification(String twitterId) throws TwitterException {
        if (alreadyFriends(twitterId)) {
            twitter.disableNotification(twitterId);
            twitter.destroyFriendship(twitterId);
        }
    }

    private boolean alreadyFriends(String twitterId) throws TwitterException {
        return twitter.existsFriendship(twitter.getScreenName(), twitterId);
    }
}