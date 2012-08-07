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


/**
 * Adapts a {@link OAuthSession} returned by the {@link OAuthSessionManager} to a Spring Security {@link OAuthProviderToken}.
 * @author Keith Donald
 * @see OAuthSessionManagerProviderTokenServices
 */
@SuppressWarnings("serial")
class OAuthSessionProviderToken  { //implements OAuthProviderToken {

	private OAuthSession session;

	public OAuthSessionProviderToken(OAuthSession session) {
		this.session = session;
	}

	public String getConsumerKey() {
		return session.getApiKey();
	}
	
	public String getValue() {
		return session.getRequestToken();
	}
	
	public String getSecret() {
		return session.getSecret();
	}
	
	public String getCallbackUrl() {
		return session.getCallbackUrl();
	}

	public String getVerifier() {
		return session.getVerifier();
	}

	public boolean isAccessToken() {
		return false;
	}			
}