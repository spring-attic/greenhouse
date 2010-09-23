package com.springsource.greenhouse.develop.oauth;

import org.springframework.security.oauth.provider.token.OAuthProviderToken;

@SuppressWarnings("serial")
class OAuthSessionProviderToken implements OAuthProviderToken {

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