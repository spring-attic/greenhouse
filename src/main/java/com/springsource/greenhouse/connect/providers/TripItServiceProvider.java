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

import org.springframework.social.tripit.TripItApi;
import org.springframework.social.tripit.TripItTemplate;

import com.springsource.greenhouse.connect.AbstractServiceProvider;
import com.springsource.greenhouse.connect.AccountConnectionRepository;
import com.springsource.greenhouse.connect.OAuthToken;
import com.springsource.greenhouse.connect.ServiceProviderParameters;

/**
 * TripIt ServiceProvider implementation.
 * @author Craig Walls
 */
public final class TripItServiceProvider extends AbstractServiceProvider<TripItApi> {
	
	public TripItServiceProvider(ServiceProviderParameters parameters, AccountConnectionRepository connectionRepository) {
		super(parameters, connectionRepository);
	}

	protected TripItApi createServiceOperations(OAuthToken accessToken) {
		if (accessToken == null) {
			throw new IllegalStateException("Cannot access TripIt without an access token");
		}
		return new TripItTemplate(getApiKey(), getSecret(), accessToken.getValue(), accessToken.getSecret());
	}

	protected String fetchProviderAccountId(TripItApi tripIt) {
		return tripIt.getProfileId();
	}

	protected String buildProviderProfileUrl(String tripItId, TripItApi tripIt) {
		return tripIt.getProfileUrl();
	}
	
}