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
import net.chrissearle.spring.twitter.spring.AbstractTwitter4JSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:twitter4j.xml"})
public class TestAmFollowingInactive {
    private static final String TEST_FRIEND = "csnet";

    @Autowired
    private FollowService followService;

    private Boolean activeFlag;

    @Before
    public void setInactive() {
        final AbstractTwitter4JSupport abstractTwitter4JSupport = (AbstractTwitter4JSupport) followService;

        activeFlag = abstractTwitter4JSupport.isActive();

        abstractTwitter4JSupport.configure(false);
    }

    @After
    public void setActiveFlag() {
        final AbstractTwitter4JSupport abstractTwitter4JSupport = (AbstractTwitter4JSupport) followService;

        abstractTwitter4JSupport.configure(activeFlag);
    }

    @Test
    public void testAmFollowing() {
        followService.follow(TEST_FRIEND);

        List<String> ids = followService.amFollowing();

        assertTrue("User list should be empty when inactive", ids.size() == 0);
    }
}
