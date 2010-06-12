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

import net.chrissearle.spring.twitter.service.FollowService;
import net.chrissearle.spring.twitter.service.TimelineService;
import net.chrissearle.spring.twitter.service.TweetService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:twitter4j.xml"})
public class TestTweet {
    private static final String TEST_TWEET = "Unit testing - test tweet text";

    @Autowired
    private TweetService tweetService;

    @Autowired
    private TimelineService timelineService;

    public TestTweet() {
        System.setProperty("CONSTRETTO_TAGS", "active");
    }

    @Test
    public void testTweet() {
        tweetService.tweet(TEST_TWEET);

        assertEquals("Tweet not seen on timeline", TEST_TWEET, timelineService.getLastStatus());
    }
}
