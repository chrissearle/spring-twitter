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

import net.chrissearle.spring.twitter.service.UserExistanceService;
import net.chrissearle.spring.twitter.spring.AbstractTwitter4JSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:twitter4j.xml"})
public class TestUserExistanceInactive {
    private static final String TEST_FRIEND = "csnet";
    private static final String TEST_IMAGINARY_FRIEND = "csnet_this_user_should_not_exist";

    @Autowired
    private UserExistanceService userExistanceService;

    private Boolean activeFlag;

    @Before
    public void setInactive() {
        final AbstractTwitter4JSupport abstractTwitter4JSupport = (AbstractTwitter4JSupport) userExistanceService;

        activeFlag = abstractTwitter4JSupport.isActive();

        abstractTwitter4JSupport.configure(false);
    }

    @After
    public void setActiveFlag() {
        final AbstractTwitter4JSupport abstractTwitter4JSupport = (AbstractTwitter4JSupport) userExistanceService;

        abstractTwitter4JSupport.configure(activeFlag);
    }

    @Test
    public void testExists() {
        assertTrue("When inactive we should just say yes for user check", userExistanceService.checkIfUserExists(TEST_FRIEND));
    }

    @Test
    public void testNotExists() {
        assertTrue("When inactive we should just say yes for user check", userExistanceService.checkIfUserExists(TEST_IMAGINARY_FRIEND));
    }
}
