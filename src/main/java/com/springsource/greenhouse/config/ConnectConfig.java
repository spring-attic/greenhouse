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
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
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

/**
 * Spring Social Configuration.
 * Allows Greenhouse users to connect to SaaS providers such as Twitter and Facebook.
 * @author Keith Donald
 */
@Configuration
public class ConnectConfig extends EnvironmentAwareConfig {

	@Inject
	private DataSource dataSource;
	
	@Inject
	private TextEncryptor textEncryptor;
	
	/**
	 * The locator for SaaS provider connection factories.
	 * When support for a new provider is added to Greenhouse, simply register the corresponding {@link ConnectionFactory} here.
	 * The current Environment is used to lookup the credentials assigned to the Greenhouse application by each provider during application registration.
	 */
	@Bean
	public ConnectionFactoryLocator connectionFactoryLocator() {
		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
		registry.addConnectionFactory(new TwitterConnectionFactory(environment.getProperty("twitter.consumerKey"), environment.getProperty("twitter.consumerSecret")));
		registry.addConnectionFactory(new FacebookConnectionFactory(environment.getProperty("facebook.appId"), environment.getProperty("facebook.appSecret")));
		registry.addConnectionFactory(new LinkedInConnectionFactory(environment.getProperty("linkedin.consumerKey"), environment.getProperty("linkedin.consumerSecret")));		
		registry.addConnectionFactory(new TripItConnectionFactory(environment.getProperty("tripit.consumerKey"), environment.getProperty("tripit.consumerSecret")));
		return registry;
	}
	
	/**
	 * THe shared store for users' connection information.
	 * Uses a RDBMS-based store accessed with Spring's JdbcTemplate.
	 * The returned repository encrypts the data using the configured {@link TextEncryptor}.
	 */
	@Bean
	public UsersConnectionRepository usersConnectionRepository() {
		return new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator(), textEncryptor);
	}

	/**
	 * A request-scoped bean that provides the data access interface to the current user's connections
	 * Note this bean is not a scoped-proxy, so it is NOT instantiated at application startup and may only be obtained in the context of a web request.
	 * This is achieved by injecting a reference to javax.inject.Provider&lt;ConnectionRepository&gt; into other objects that need a {@link ConnectionRepository}.
	 */
	@Bean
	@Scope(value="request")
	public ConnectionRepository connectionRepository() {
		Account account = AccountUtils.getCurrentAccount();
		if (account == null) {
			throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
		}
		return usersConnectionRepository().createConnectionRepository(account.getName());
	}

	/**
	 * A request-scoped bean representing the API binding to Facebook for the current user.
	 * Note this bean is not a scoped-proxy, so it is NOT instantiated at application startup and may only be obtained in the context of a web request.
	 * This is achieved by injecting a reference to javax.inject.Provider&lt;Facebook&gt; into other objects.
	 * Returns null if the current user is not connected to Facebook.
	 */
	@Bean
	@Scope(value="request")	
	public Facebook facebook() {
		Connection<Facebook> connection = connectionRepository().findPrimaryConnectionToApi(Facebook.class);
		return connection != null ? connection.getApi() : null;
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
		Connection<Twitter> connection = connectionRepository().findPrimaryConnectionToApi(Twitter.class);
		return connection != null ? connection.getApi() : new TwitterTemplate();
	}

	/**
	 * The Spring MVC Controller that coordinates connections to service providers on behalf of users.
	 * @param connectionRepositoryProvider needed to persist new connections
	 * @param profilePictureService needed by the {@link FacebookConnectInterceptor} to make the user's Facebook profile picture their Greenhouse profile picture.
	 */
	@Bean
	public ConnectController connectController(Provider<ConnectionRepository> connectionRepositoryProvider, ProfilePictureService profilePictureService) {
		ConnectController controller = new ConnectController(getSecureUrl(), connectionFactoryLocator(), connectionRepositoryProvider);
		controller.addInterceptor(new FacebookConnectInterceptor(profilePictureService));
		controller.addInterceptor(new TwitterConnectInterceptor());
		return controller;
	}

	/**
	 * The Spring MVC Controller that coordinates "sign-in with {provider}" attempts.
	 * @param connectionFactoryLocatorProvider needed to create connections and restore them from their persistent form.
	 * @param connectionRepositoryProvider needed to persist new connections
	 * @param profilePictureService needed by the {@link FacebookConnectInterceptor} to make the user's Facebook profile picture their Greenhouse profile picture.
	 */
	@Bean
	public ProviderSignInController providerSignInController(Provider<ConnectionFactoryLocator> connectionFactoryLocatorProvider, Provider<ConnectionRepository> connectionRepositoryProvider, SignInService signInService) {
		return new ProviderSignInController(getSecureUrl(), connectionFactoryLocatorProvider, usersConnectionRepository(), connectionRepositoryProvider, signInService);
	}
	
	// internal helpers
	
	private String getSecureUrl() {
		return environment.getProperty("application.secureUrl");
	}
	
}
