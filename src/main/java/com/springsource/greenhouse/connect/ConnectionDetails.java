package com.springsource.greenhouse.connect;

public class ConnectionDetails {
	
	private final String accessToken;
	
	private final String providerAccountId;

	public ConnectionDetails(String accessToken, String providerAccountId) {
		this.accessToken = accessToken;
		this.providerAccountId = providerAccountId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getProviderAccountId() {
		return providerAccountId;
	}
	
}
