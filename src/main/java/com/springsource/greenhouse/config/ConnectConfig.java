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
package com.springsource.greenhouse.config;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.signin.web.ProviderSignInController;
import org.springframework.social.connect.signin.web.SignInService;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.tripit.connect.TripItConnectionFactory;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountUtils;
import com.springsource.greenhouse.connect.FacebookConnectInterceptor;
import com.springsource.greenhouse.connect.TwitterConnectInterceptor;
import com.springsource.greenhouse.members.ProfilePictureService;

@Configuration
public class ConnectConfig {

	@Inject
	private DataSource dataSource;
	
	@Inject
	private TextEncryptor textEncryptor;
	
	@Inject
	private Environment environment;

	@Bean
	public ConnectionFactoryLocator connectionFactoryLocator() {
		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
		registry.addConnectionFactory(new TwitterConnectionFactory(environment.getProperty("twitter.consumerKey"), environment.getProperty("twitter.consumerSecret")));
		registry.addConnectionFactory(new FacebookConnectionFactory(environment.getProperty("facebook.appId"), environment.getProperty("facebook.appSecret")));
		registry.addConnectionFactory(new LinkedInConnectionFactory(environment.getProperty("linkedin.consumerKey"), environment.getProperty("linkedin.consumerSecret")));		
		registry.addConnectionFactory(new TripItConnectionFactory(environment.getProperty("tripit.consumerKey"), environment.getProperty("tripit.consumerSecret")));
		return registry;
	}
	
	@Bean
	public UsersConnectionRepository usersConnectionRepository() {
		return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator(), textEncryptor);
	}

	@Bean
	@Scope(value="request")
	public ConnectionRepository connectionRepository() {
		Account account = AccountUtils.getCurrentAccount();
		if (account == null) {
			throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
		}
		return usersConnectionRepository().createConnectionRepository(account.getName());
	}
	
	@Bean
	@Scope(value="request")	
	public Facebook facebook() {
		Connection<Facebook> connection = connectionRepository().findPrimaryConnectionToApi(Facebook.class);
		return connection != null ? connection.getApi() : null;
	}

	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)	
	public Twitter twitter() {
		Connection<Twitter> connection = connectionRepository().findPrimaryConnectionToApi(Twitter.class);
		return connection != null ? connection.getApi() : new TwitterTemplate();
	}
	
	@Bean
	public ConnectController connectController(Provider<ConnectionRepository> connectionRepositoryProvider, ProfilePictureService profilePictureService) {
		ConnectController controller = new ConnectController(getSecureUrl(), connectionFactoryLocator(), connectionRepositoryProvider);
		controller.addInterceptor(new FacebookConnectInterceptor(profilePictureService));
		controller.addInterceptor(new TwitterConnectInterceptor());
		return controller;
	}
	
	@Bean
	public ProviderSignInController providerSignInController(Provider<ConnectionFactoryLocator> connectionFactoryLocatorProvider, Provider<ConnectionRepository> connectionRepositoryProvider, SignInService signInService) {
		return new ProviderSignInController(getSecureUrl(), connectionFactoryLocatorProvider, usersConnectionRepository(), connectionRepositoryProvider, signInService);
	}
	
	private String getSecureUrl() {
		return environment.getProperty("application.secureUrl");
	}
	
}
