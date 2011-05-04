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
package com.springsource.greenhouse.develop;

/**
 * A connection between a client Application and a member Account.
 * While the connection remains established, the client Application is authorized to read and update data on behalf of the member.
 * This is achieved by the client application presenting the access token in a request to access a protected resource.
 * @author Keith Donald
 */
public final class AppConnection {

	private final Long accountId;
	
	private final String apiKey;

	private final String accessToken;
	
	private final String secret;
	
	public AppConnection(Long accountId, String apiKey, String accessToken, String secret) {
		this.accountId = accountId;
		this.apiKey = apiKey;
		this.accessToken = accessToken;
		this.secret = secret;
	}

	/**
	 * The member account id.
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * The api key identifying the client application.
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * The access token identifying the connection.
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * The access token secret, used for signature verification for OAuth 1.0-based clients.
	 */
	public String getSecret() {
		return secret;
	}

}
