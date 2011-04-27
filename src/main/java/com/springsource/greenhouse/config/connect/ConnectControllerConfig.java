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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.signin.web.ProviderSignInController;
import org.springframework.social.connect.signin.web.SignInService;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ConnectInterceptor;

import com.springsource.greenhouse.connect.FacebookConnectInterceptor;
import com.springsource.greenhouse.connect.TwitterConnectInterceptor;
import com.springsource.greenhouse.members.ProfilePictureService;

@Configuration
public class ConnectControllerConfig {

	@Inject
	private Environment environment;
	
	@Bean
	public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator, 
			Provider<ConnectionRepository> connectionRepositoryProvider, ProfilePictureService profilePictureService) {
		ConnectController controller = new ConnectController(getSecureUrl(), connectionFactoryLocator, connectionRepositoryProvider);
		List<ConnectInterceptor<?>> interceptors = new ArrayList<ConnectInterceptor<?>>();
		interceptors.add(new FacebookConnectInterceptor(profilePictureService));
		interceptors.add(new TwitterConnectInterceptor());
		controller.setInterceptors(interceptors);
		return controller;
	}
	
	@Bean
	public ProviderSignInController providerSignInController(Provider<ConnectionFactoryLocator> connectionFactoryLocatorProvider,
			UsersConnectionRepository usersConnectionRepository, Provider<ConnectionRepository> connectionRepositoryProvider, SignInService signInService) {
		return new ProviderSignInController(getSecureUrl(), connectionFactoryLocatorProvider, usersConnectionRepository, connectionRepositoryProvider, signInService);
	}
	
	private String getSecureUrl() {
		return environment.getProperty("application.secureUrl");
	}
	
}
