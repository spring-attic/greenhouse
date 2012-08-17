/*
 * Copyright 2012 the original author or authors.
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

import javax.inject.Inject;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.BaseClientDetails;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

import com.springsource.greenhouse.develop.App;
import com.springsource.greenhouse.develop.AppRepository;
import com.springsource.greenhouse.develop.InvalidApiKeyException;

/**
 * Adapts App records returned by an {@link AppRepository} to Spring Security OAuth2 {@link ClientDetails}.
 * Allows an AppRepository to serve as the source for OAuth2 Clients known to the Spring Security OAuth2 provider.
 * @author Craig Walls
 */
@Service("clientDetails")
public class AppClientDetailsService implements ClientDetailsService {

	private final AppRepository appRepository;
	
	@Inject
	public AppClientDetailsService(AppRepository appRepository) {
		this.appRepository = appRepository;
	}

	@Override
	public ClientDetails loadClientByClientId(String appId) throws OAuth2Exception {
		try {
			return clientDetailsFor(appRepository.findAppByApiKey(appId));
		} catch (InvalidApiKeyException e) {
			throw new OAuth2Exception("Invalid OAuth App ID " + appId, e);
		}
	}

	private ClientDetails clientDetailsFor(App app) {
		return new AppClientDetails(app);
	}
	
	@SuppressWarnings("serial")
	private static class AppClientDetails extends BaseClientDetails {

		public AppClientDetails(App app) {
			// TODO Consider putting hard-coded values in DB instead.
			super(app.getApiKey(), "greenhouseApi", "read,write", "authorization_code,token,password", "ROLE_CLIENT", app.getCallbackUrl());
			setClientSecret(app.getSecret());
		}

	}

}