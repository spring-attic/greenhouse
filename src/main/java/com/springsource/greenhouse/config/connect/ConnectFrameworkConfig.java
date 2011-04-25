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
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.MultiUserServiceProviderConnectionRepository;
import org.springframework.social.connect.ServiceProviderConnectionFactoryLocator;
import org.springframework.social.connect.ServiceProviderConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcMultiUserServiceProviderConnectionRepository;
import org.springframework.social.connect.support.MapServiceProviderConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookServiceProviderConnectionFactory;
import org.springframework.social.linkedin.connect.LinkedInServiceProviderConnectionFactory;
import org.springframework.social.tripit.connect.TripItServiceProviderConnectionFactory;
import org.springframework.social.twitter.connect.TwitterServiceProviderConnectionFactory;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountUtils;

@Configuration
public class ConnectFrameworkConfig {

	@Inject
	private DataSource dataSource;
	
	@Inject
	private TextEncryptor textEncryptor;
	
	@Inject
	private Environment environment;
	
	@Bean
	public ServiceProviderConnectionFactoryLocator connectionFactoryLocator() {
		MapServiceProviderConnectionFactoryRegistry registry = new MapServiceProviderConnectionFactoryRegistry();
		registry.addConnectionFactory(new TwitterServiceProviderConnectionFactory(environment.getProperty("twitter.consumerKey"), environment.getProperty("twitter.consumerSecret")));
		registry.addConnectionFactory(new FacebookServiceProviderConnectionFactory(environment.getProperty("facebook.appId"), environment.getProperty("facebook.appSecret")));
		registry.addConnectionFactory(new LinkedInServiceProviderConnectionFactory(environment.getProperty("linkedin.consumerKey"), environment.getProperty("linkedin.consumerSecret")));		
		registry.addConnectionFactory(new TripItServiceProviderConnectionFactory(environment.getProperty("tripit.consumerKey"), environment.getProperty("tripit.consumerSecret")));
		return registry;
	}
	
	@Bean
	public MultiUserServiceProviderConnectionRepository multiUserConnectionRepository() {
		return new JdbcMultiUserServiceProviderConnectionRepository(dataSource, connectionFactoryLocator(), textEncryptor);
	}

	@Bean
	@Scope(value="request")
	public ServiceProviderConnectionRepository connectionRepository() {
		Account account = AccountUtils.getCurrentAccount();
		if (account == null) {
			throw new IllegalStateException("Unable to get a ServiceProviderConnectionRepository: no user signed in");
		}
		return multiUserConnectionRepository().createConnectionRepository(account.getName());
	}
	
}