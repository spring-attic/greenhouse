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

class StandardOAuthSession implements OAuthSession {

	private String apiKey;
	
	private String callbackUrl;
	
	private String requestToken;
	
	private String secret;
	
	private Long authorizingAccountId;
	
	private String verifier;
	
	public StandardOAuthSession(String apiKey, String callbackUrl, String requestToken, String secret) {
		this(apiKey, callbackUrl, requestToken, secret, null, null);
	}

	public StandardOAuthSession(String apiKey, String callbackUrl, String requestToken, String secret, Long authorizingAccountId, String verifier) {
		this.apiKey = apiKey;
		this.callbackUrl = callbackUrl;
		this.requestToken = requestToken;
		this.secret = secret;
		this.authorizingAccountId = authorizingAccountId;
		this.verifier = verifier;
	}

	public String getApiKey() {
		return apiKey;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public String getRequestToken() {
		return requestToken;
	}

	public String getSecret() {
		return secret;
	}

	public void authorize(Long authorizingAccountId, String verifier) {
		this.authorizingAccountId = authorizingAccountId;
		this.verifier = verifier;
	}

	public boolean authorized() {
		return authorizingAccountId != null;
	}

	public Long getAuthorizingAccountId() {
		return authorizingAccountId;
	}

	public String getVerifier() {
		return verifier;
	}

}