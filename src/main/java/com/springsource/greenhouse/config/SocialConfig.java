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
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInAttempt;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.tripit.connect.TripItConnectionFactory;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.AccountUtils;
import com.springsource.greenhouse.connect.AccountSignInAdapter;
import com.springsource.greenhouse.connect.FacebookConnectInterceptor;
import com.springsource.greenhouse.connect.TwitterConnectInterceptor;
import com.springsource.greenhouse.members.ProfilePictureService;

/**
 * Spring Social Configuration.
 * Allows Greenhouse users to connect to SaaS providers such as Twitter and Facebook.
 * @author Keith Donald
 */
@Configuration
public class SocialConfig {

	@Inject
	private DataSource dataSource;
	
	@Inject
	private TextEncryptor textEncryptor;

	@Inject
	private Environment environment;
	
	/**
	 * The locator for SaaS provider connection factories.
	 * When support for a new provider is added to Greenhouse, simply register the corresponding {@link ConnectionFactory} here.
	 * The current Environment is used to lookup the credentials assigned to the Greenhouse application by each provider during application registration.
	 * This bean is defined as a scoped-proxy so it can be serialized in support of {@link ProviderSignInAttempt provier sign-in attempts}.
	 */
	@Bean
	@Scope(value="singleton", proxyMode=ScopedProxyMode.INTERFACES)	
	public ConnectionFactoryLocator connectionFactoryLocator() {
		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
		registry.addConnectionFactory(new TwitterConnectionFactory(environment.getProperty("twitter.consumerKey"), environment.getProperty("twitter.consumerSecret")));
		registry.addConnectionFactory(new FacebookConnectionFactory(environment.getProperty("facebook.appId"), environment.getProperty("facebook.appSecret")));
		registry.addConnectionFactory(new LinkedInConnectionFactory(environment.getProperty("linkedin.consumerKey"), environment.getProperty("linkedin.consumerSecret")));		
		registry.addConnectionFactory(new TripItConnectionFactory(environment.getProperty("tripit.consumerKey"), environment.getProperty("tripit.consumerSecret")));
		return registry;
	}
	
	/**
	 * The shared store for users' connection information.
	 * Uses a RDBMS-based store accessed with Spring's JdbcTemplate.
	 * The returned repository encrypts the data using the configured {@link TextEncryptor}.
	 */
	@Bean
	public UsersConnectionRepository usersConnectionRepository() {
		return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator(), textEncryptor);
	}

	/**
	 * A request-scoped bean that provides the data access interface to the current user's connections.
	 * Since it is a scoped-proxy, references to this bean MAY be injected at application startup time.
	 * If no user is authenticated when the target is resolved, an {@link IllegalStateException} is thrown.
	 * @throws IllegalStateException when no user is authenticated.
	 */
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)	
	public ConnectionRepository connectionRepository() {
		Account account = AccountUtils.getCurrentAccount();
		if (account == null) {
			throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
		}
		return usersConnectionRepository().createConnectionRepository(account.getId().toString());
	}

	/**
	 * A request-scoped bean representing the API binding to Facebook for the current user.
	 * Since it is a scoped-proxy, references to this bean MAY be injected at application startup time.
	 * The target is an authorized {@link Facebook} instance if the current user has connected his or her account with a Facebook account.
	 * Otherwise, the target is a new FacebookTemplate that can invoke operations that do not require authorization.
	 */
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)	
	public Facebook facebook() {
		Connection<Facebook> facebook = connectionRepository().findPrimaryConnection(Facebook.class);
		return facebook != null ? facebook.getApi() : new FacebookTemplate();
	}

	/**
	 * A proxy to the request-scoped API binding to Twitter for the current user.
	 * Since it is a scoped-proxy, references to this bean MAY be injected at application startup time.
	 * The target is an authorized {@link Twitter} instance if the current user has connected his or her account with a Twitter account.
	 * Otherwise, the target is a new TwitterTemplate that can invoke operations that do not require authorization.
	 */
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)	
	public Twitter twitter() {
		Connection<Twitter> twitter = connectionRepository().findPrimaryConnection(Twitter.class);
		return twitter != null ? twitter.getApi() : new TwitterTemplate();
	}

	/**
	 * The Spring MVC Controller that coordinates connections to service providers on behalf of users.
	 * @param connectionRepositoryProvider needed to persist new connections
	 * @param profilePictureService needed by the {@link FacebookConnectInterceptor} to make the user's Facebook profile picture their Greenhouse profile picture.
	 */
	@Bean
	public ConnectController connectController(ProfilePictureService profilePictureService) {
		ConnectController controller = new ConnectController(connectionFactoryLocator(), connectionRepository());
		controller.addInterceptor(new FacebookConnectInterceptor(profilePictureService));
		controller.addInterceptor(new TwitterConnectInterceptor());
		return controller;
	}

	/**
	 * The Spring MVC Controller that coordinates "sign-in with {provider}" attempts.
	 * @param accountRepository the account repository that can load user Account objects given an account id.
	 */
//	@Bean
	public ProviderSignInController providerSignInController(AccountRepository accountRepository, RequestCache requestCache) {
		return new ProviderSignInController(connectionFactoryLocator(), usersConnectionRepository(), new AccountSignInAdapter(accountRepository, requestCache));
	}
	
}
