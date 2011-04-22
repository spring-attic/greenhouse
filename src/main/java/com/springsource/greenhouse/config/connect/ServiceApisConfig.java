/*
 * Copyright 2010-2011 the original author or authors.
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

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.connect.ServiceProviderConnection;
import org.springframework.social.connect.ServiceProviderConnectionRepository;
import org.springframework.social.facebook.api.FacebookApi;
import org.springframework.social.twitter.api.TwitterApi;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration
public class ServiceApisConfig {

	@Inject
	private ServiceProviderConnectionRepository connectionRepository;
	
	@Bean
	@Scope(value="request")	
	public FacebookApi facebookApi() {
		ServiceProviderConnection<FacebookApi> connection = connectionRepository.findPrimaryConnectionToServiceApi(FacebookApi.class);
		return connection != null ? connection.getServiceApi() : null;
	}

	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)	
	public TwitterApi twitterApi() {
		ServiceProviderConnection<TwitterApi> connection = connectionRepository.findPrimaryConnectionToServiceApi(TwitterApi.class);
		return connection != null ? connection.getServiceApi() : new TwitterTemplate();
	}
	
}