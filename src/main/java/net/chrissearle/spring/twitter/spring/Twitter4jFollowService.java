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
import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service("followService")
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

        if (isActive()) {
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

        if (isActive()) {
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

        if (isActive()) {
            removeTwitterFriendshipAndNotification(twitterUserId);
        } else {
            if (logger.isLoggable(Level.INFO)) {
                logger.info("Twitter disabled");
            }
        }
    }

    @Override
    public List<String> amFollowing() {
        if (logger.isLoggable(Level.INFO)) {
            logger.info("Getting following list");
        }

        if (isActive()) {
            return retrieveFollowing();
        }

        // Nothing found - return empty list.
        return new ArrayList<String>();
    }

    @Override
    public List<String> followingMe() {
        if (logger.isLoggable(Level.INFO)) {
            logger.info("Getting followers list");
        }

        if (isActive()) {
            return retrieveFollowers();
        }

        // Nothing found - return empty list.
        return new ArrayList<String>();
    }

    private List<String> retrieveFollowing() {
        List<String> usernames = new ArrayList<String>();

        try {

            usernames = populateUsernameList(twitter.getFriendsIDs());
        } catch (TwitterException e) {
            final String message = new StringBuilder().append("Unable to retrieve user details due to ").append(e.getMessage()).toString();

            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(message);
            }

            throw new TwitterServiceException(message, e);
        }

        return usernames;
    }


    private List<String> retrieveFollowers() {
        List<String> usernames = new ArrayList<String>();

        try {

            usernames = populateUsernameList(twitter.getFollowersIDs());
        } catch (TwitterException e) {
            final String message = new StringBuilder().append("Unable to retrieve user details due to ").append(e.getMessage()).toString();

            if (logger.isLoggable(Level.WARNING)) {
                logger.warning(message);
            }

            throw new TwitterServiceException(message, e);
        }

        return usernames;
    }

    private List<String> populateUsernameList(IDs ids) throws TwitterException {
        List<String> usernames = new ArrayList<String>();

        for (int id : ids.getIDs()) {
            User user = twitter.showUser(id);

            if (logger.isLoggable(Level.FINE)) {
                logger.fine(new StringBuilder().append("Saw user: ").append(user.getName()).append(" ").append(user.getScreenName()).toString());
            }

            usernames.add(user.getScreenName());
        }

        return usernames;
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