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
package com.springsource.greenhouse.connect;

import org.springframework.web.util.UriTemplate;

/**
 * Holder for common ServiceProvider parameters.
 * Used by {@link AbstractServiceProvider} to access provider state in a manageable way.
 * @author Keith Donald
 */
public class ServiceProviderParameters {
	
	private final String name;
	
	private final String displayName;
	
	private final String apiKey;
	
	private final String secret;
	
	private final Long appId;
	
	private final String requestTokenUrl;
	
	private final UriTemplate authorizeUrl;
	
	private final String accessTokenUrl;

	public ServiceProviderParameters(String name, String displayName, String apiKey, String secret, Long appId, String requestTokenUrl, String authorizeUrl, String accessTokenUrl) {
		this.name = name;
		this.displayName = displayName;
		this.apiKey = apiKey;
		this.secret = secret;
		this.appId = appId;
		this.requestTokenUrl = requestTokenUrl;
		this.authorizeUrl = authorizeUrl != null ? new UriTemplate(authorizeUrl) : null;
		this.accessTokenUrl = accessTokenUrl;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public String getApiKey() {
		return apiKey;
	}

	public String getSecret() {
		return secret;
	}

	public Long getAppId() {
		return appId;
	}

	public String getRequestTokenUrl() {
		return requestTokenUrl;
	}

	public UriTemplate getAuthorizeUrl() {
		return authorizeUrl;
	}

	public String getAccessTokenUrl() {
		return accessTokenUrl;
	}

}