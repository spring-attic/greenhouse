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

import org.springframework.social.twitter.TwitterApi;
import org.springframework.social.twitter.TwitterTemplate;

import com.springsource.greenhouse.connect.AbstractServiceProvider;
import com.springsource.greenhouse.connect.AccountConnectionRepository;
import com.springsource.greenhouse.connect.OAuthToken;
import com.springsource.greenhouse.connect.ServiceProviderParameters;

/**
 * Twitter ServiceProvider implementation.
 * @author Keith Donald
 * @author Craig Walls
 */
public final class TwitterServiceProvider extends AbstractServiceProvider<TwitterApi> {
	
	public TwitterServiceProvider(ServiceProviderParameters parameters, AccountConnectionRepository connectionRepository) {
		super(parameters, connectionRepository);
	}

	protected TwitterApi createServiceOperations(OAuthToken accessToken) {
		return accessToken != null ? new TwitterTemplate(getApiKey(), getSecret(), accessToken.getValue(), accessToken.getSecret()) : new TwitterTemplate();
	}

	protected String fetchProviderAccountId(TwitterApi twitter) {
		return twitter.getProfileId();
	}

	protected String buildProviderProfileUrl(String screenName, TwitterApi twitter) {
		return "http://www.twitter.com/" + screenName;
	}
	
}