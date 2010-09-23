package com.springsource.greenhouse.connect;

public class ConnectionDetails {
	
	private final String accessToken;
	
	private final String secret;

	private final String providerAccountId;

	public ConnectionDetails(String accessToken, String secret, String providerAccountId) {
		this.accessToken = accessToken;
		this.secret = secret;
		this.providerAccountId = providerAccountId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getSecret() {
		return secret;
	}

	public String getProviderAccountId() {
		return providerAccountId;
	}
	
}
