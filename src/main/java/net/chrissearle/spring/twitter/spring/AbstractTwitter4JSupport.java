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

import org.constretto.annotation.Configuration;
import org.constretto.annotation.Configure;
import twitter4j.Twitter;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractTwitter4JSupport {
    protected Twitter twitter;
    protected Boolean twitterActiveFlag;

    private final Logger logger = Logger.getLogger(Twitter4jTweetService.class.getName());

    public AbstractTwitter4JSupport(Twitter twitter) {
        this.twitter = twitter;
    }

    @Configure
    public void configure(@Configuration(expression = "twitter.active") Boolean active) {
        if (logger.isLoggable(Level.INFO)) {
            if (active) {
                logger.info("Initializing twitter service : active");
            } else {
                logger.info("Initializing twitter service : inactive");
            }
        }

        this.twitterActiveFlag = active;
    }

    public Boolean isActive() {
        return this.twitterActiveFlag;
    }
}
