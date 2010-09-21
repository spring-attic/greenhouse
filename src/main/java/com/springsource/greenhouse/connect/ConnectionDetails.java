package com.springsource.greenhouse.connect;

public class ConnectionDetails {
	
	private final String accessToken;
	
	private final String accessTokenSecret;

	private final String providerAccountId;

	public ConnectionDetails(String accessToken, String accessTokenSecret, String providerAccountId) {
		this.accessToken = accessToken;
		this.accessTokenSecret = accessTokenSecret;
		this.providerAccountId = providerAccountId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	public String getProviderAccountId() {
		return providerAccountId;
	}
	
}
