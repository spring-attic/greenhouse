package com.springsource.greenhouse.connect;

public final class OAuthToken {
	
	private final String value;
	
	private final String secret;

	public OAuthToken(String value, String secret) {
		this.value = value;
		this.secret = secret;
	}

	public String getValue() {
		return value;
	}

	public String getSecret() {
		return secret;
	}
	
}
