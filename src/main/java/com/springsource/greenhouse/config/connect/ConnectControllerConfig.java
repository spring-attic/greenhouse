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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.support.ConnectionRepository;
import org.springframework.social.facebook.web.FacebookSigninController;
import org.springframework.social.web.connect.ConnectController;
import org.springframework.social.web.connect.ConnectInterceptor;
import org.springframework.social.web.connect.SignInControllerGateway;

import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.connect.FacebookConnectInterceptor;
import com.springsource.greenhouse.connect.TwitterConnectInterceptor;
import com.springsource.greenhouse.members.ProfilePictureService;
import com.springsource.greenhouse.signin.AccountSigninGateway;

@Configuration
public class ConnectControllerConfig {

	@Bean
	public ConnectController connectController(@Value("#{environment['application.secureUrl']}") String applicationUrl,
			ProfilePictureService profilePictureService) {
		ConnectController controller = new ConnectController(applicationUrl);
		List<ConnectInterceptor<?>> interceptors = new ArrayList<ConnectInterceptor<?>>();
		interceptors.add(new FacebookConnectInterceptor(profilePictureService));
		interceptors.add(new TwitterConnectInterceptor());
		controller.setInterceptors(interceptors);
		return controller;
	}
	
	@Bean
	public FacebookSigninController facebookSigninController(@Value("#{environment['facebook.appId']}") String appId,
			@Value("#{environment['facebook.appSecret']}") String appSecret,
			ConnectionRepository connectionRepository, AccountRepository accountRepository) {
		SignInControllerGateway signinGateway = new AccountSigninGateway(accountRepository);
		return new FacebookSigninController(connectionRepository, signinGateway, appId, appSecret);
	}
	
}
