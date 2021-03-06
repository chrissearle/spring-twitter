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
import org.springframework.beans.factory.FactoryBean;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;

public class TwitterFactoryBean
        implements FactoryBean {
    private String consumerKey;
    private String consumerSecret;
    private AccessToken accessToken;

    @Configure
    public void configure(@Configuration(expression = "twitter.consumer.key") String consumerKey,
                          @Configuration(expression = "twitter.consumer.secret") String consumerSecret,
                          @Configuration(expression = "twitter.token") String token,
                          @Configuration(expression = "twitter.token.secret") String tokenSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = new AccessToken(token, tokenSecret);
    }

    public Twitter getObject() {
        return new TwitterFactory().getOAuthAuthorizedInstance(this.consumerKey,
                this.consumerSecret, this.accessToken);
    }

    public Class<Twitter> getObjectType() {
        return Twitter.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
