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
package com.springsource.greenhouse.connect.providers;

import org.springframework.social.facebook.FacebookApi;
import org.springframework.social.facebook.FacebookTemplate;

import com.springsource.greenhouse.connect.AbstractServiceProvider;
import com.springsource.greenhouse.connect.AccountConnectionRepository;
import com.springsource.greenhouse.connect.OAuthToken;
import com.springsource.greenhouse.connect.ServiceProviderParameters;

/**
 * Facebook ServiceProvider implementation.
 * @author Keith Donald
 */
public final class FacebookServiceProvider extends AbstractServiceProvider<FacebookApi> {
	
	public FacebookServiceProvider(ServiceProviderParameters parameters, AccountConnectionRepository connectionRepository) {
		super(parameters, connectionRepository);
	}

	protected FacebookApi createServiceOperations(OAuthToken accessToken) {
		if (accessToken == null) {
			throw new IllegalStateException("Cannot access Facebook without an access token");
		}
		return new FacebookTemplate(accessToken.getValue());
	}

	protected String fetchProviderAccountId(FacebookApi facebook) {
		return facebook.getProfileId();
	}

	protected String buildProviderProfileUrl(String facebookId, FacebookApi facebook) {
		return "http://www.facebook.com/profile.php?id=" + facebookId;
	}

}