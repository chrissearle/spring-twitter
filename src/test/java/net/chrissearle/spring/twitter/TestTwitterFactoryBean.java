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

package net.chrissearle.spring.twitter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.OAuthAuthorization;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:twitter4j.xml"})
public class TestTwitterFactoryBean {

    @Autowired
    private Twitter twitter;

    @Test
    public void testConfig() throws TwitterException {
        AccessToken token = ((OAuthAuthorization) twitter.getAuthorization()).getOAuthAccessToken();

        assertEquals("Incorrect token", "26201071-WktXUN2WvmItdtgjy17YR2YyVAangq72ALmgzQX8", token.getToken());
        assertEquals("Incorrect token secret", "FSGGakSXVvswiXFbLHmP5aZSPOH7QBdZbVBQK3OU25Y", token.getTokenSecret());
    }

    @Test
    public void testScreenName() throws TwitterException {
        assertEquals("Incorrect user", "csnet_dev", twitter.getScreenName());
    }
}
