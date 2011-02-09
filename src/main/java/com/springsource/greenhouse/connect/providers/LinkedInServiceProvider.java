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

import org.springframework.social.linkedin.LinkedInOperations;
import org.springframework.social.linkedin.LinkedInTemplate;

import com.springsource.greenhouse.connect.AbstractServiceProvider;
import com.springsource.greenhouse.connect.AccountConnectionRepository;
import com.springsource.greenhouse.connect.OAuthToken;
import com.springsource.greenhouse.connect.ServiceProviderParameters;

/**
 * LinkedIn ServiceProvider implementation.
 * @author Keith Donald
 */
public final class LinkedInServiceProvider extends AbstractServiceProvider<LinkedInOperations> {
	
	public LinkedInServiceProvider(ServiceProviderParameters parameters, AccountConnectionRepository connectionRepository) {
		super(parameters, connectionRepository);
	}

	protected LinkedInOperations createServiceOperations(OAuthToken accessToken) {
		if (accessToken == null) {
			throw new IllegalStateException("Cannot access LinkedIn without an access token");
		}
		return new LinkedInTemplate(getApiKey(), getSecret(), accessToken.getValue(), accessToken.getSecret());
	}

	protected String fetchProviderAccountId(LinkedInOperations linkedIn) {
		return linkedIn.getProfileId();
	}

	protected String buildProviderProfileUrl(String linkedInId, LinkedInOperations linkedIn) {
		return linkedIn.getProfileUrl();
	}
	
}