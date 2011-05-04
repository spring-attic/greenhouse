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
package com.springsource.greenhouse.develop.oauth;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth.common.OAuthException;
import org.springframework.security.oauth.common.signature.SharedConsumerSecret;
import org.springframework.security.oauth.common.signature.SignatureSecret;
import org.springframework.security.oauth.provider.ConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetailsService;
import org.springframework.stereotype.Service;

import com.springsource.greenhouse.develop.App;
import com.springsource.greenhouse.develop.AppRepository;
import com.springsource.greenhouse.develop.InvalidApiKeyException;

/**
 * Adapts App records returned by an {@link AppRepository} to Spring Security OAuth {@link ConsumerDetails}.
 * Allows an AppRepository to serve as the source for OAuth Consumers known to the Spring Security OAuth provider.
 * @author Keith Donald
 */
@Service
public class AppConsumerDetailsService implements ConsumerDetailsService {

	private final AppRepository appRepository;
	
	@Inject
	public AppConsumerDetailsService(AppRepository appRepository) {
		this.appRepository = appRepository;
	}

	public ConsumerDetails loadConsumerByConsumerKey(final String key) throws OAuthException {
		try {
			return consumerDetailsFor(appRepository.findAppByApiKey(key));
		} catch (InvalidApiKeyException e) {
			throw new OAuthException("Invalid OAuth consumer key " + key, e);
		}
	}

	private ConsumerDetails consumerDetailsFor(App app) {
		return new AppConsumerDetails(app);
	}
	
	@SuppressWarnings("serial")
	private static class AppConsumerDetails implements ConsumerDetails {

		private App app;

		public AppConsumerDetails(App app) {
			this.app = app;
		}

		public String getConsumerName() {
			return app.getSummary().getName();
		}

		public String getConsumerKey() {
			return app.getApiKey();
		}

		@Override
		public SignatureSecret getSignatureSecret() {
			return new SharedConsumerSecret(app.getSecret());
		}
		
		public List<GrantedAuthority> getAuthorities() {
			return Collections.emptyList();
		}
		
	}

}