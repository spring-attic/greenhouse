/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.config.connect;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.facebook.FacebookApi;
import org.springframework.social.facebook.connect.FacebookServiceProvider;
import org.springframework.social.linkedin.LinkedInApi;
import org.springframework.social.linkedin.connect.LinkedInServiceProvider;
import org.springframework.social.tripit.TripItApi;
import org.springframework.social.tripit.connect.TripItServiceProvider;
import org.springframework.social.twitter.TwitterApi;
import org.springframework.social.twitter.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterServiceProvider;

import com.springsource.greenhouse.account.Account;

/**
 * Exports a bean for each Service API used by Greenhouse.
 * Note: each bean returned is a request-scoped proxy, a singleton stand-in for the "real" request-scoped object.
 * Spring calls the appropriate factory method in this class when the proxy is accessed for the first time in a web request.
 * Note: this is an alternative to defining these beans in XML.
 * Beans produced by Java factory methods can be defined elegantly using the @Bean annotation (no cglib dependency is required, either)
 * @author Keith Donald
 */
@Configuration
public class ServiceApiConfig {

	/**
	 * Returns an API that can be used to invoke Twitter on behalf of a member.
	 * @param twitterProvider the Twitter ServiceProvider
	 * @param account the member account 
	 */
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public TwitterApi twitter(TwitterServiceProvider twitterProvider, @Value("#{request.getAttribute('account')}") Account account) {
		if (account == null) {
			return new TwitterTemplate();
		}
		return twitterProvider.getConnections(account).get(0).getServiceApi();
	}

	/**
	 * Returns an API that can be used to invoke Facebook on behalf of a member.
	 * @param facebookProvider the Facebook ServiceProvider
	 * @param account the member account 
	 */
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public FacebookApi facebook(FacebookServiceProvider facebookProvider, @Value("#{request.getAttribute('account')}") Account account) {
		return facebookProvider.getConnections(accountId(account)).get(0).getServiceApi();
	}

	/**
	 * Returns an API that can be used to invoke LinkedIn on behalf of a member. 
	 * @param linkedInProvider the LinkedIn ServiceProvider
	 * @param account the member account 
	 */
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public LinkedInApi linkedIn(LinkedInServiceProvider linkedInProvider, @Value("#{request.getAttribute('account')}") Account account) {
		return linkedInProvider.getConnections(accountId(account)).get(0).getServiceApi();
	}

	/**
	 * Returns an API that can be used to invoke TripIt on behalf of a member.
	 * @param tripItProvider the TripIt ServiceProvider
	 * @param account the member account 
	 */
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)	
	public TripItApi tripIt(TripItServiceProvider tripItProvider, @Value("#{request.getAttribute('account')}") Account account) {
		return tripItProvider.getConnections(accountId(account)).get(0).getServiceApi();
	}

	// internal helpers
	
	private static Long accountId(Account account) {
		return account != null ? account.getId() : null;
	}
	
}