package com.springsource.greenhouse.account;

@SuppressWarnings("serial")
public final class InvalidAccessTokenException extends AccountException {

	private String accessToken;
	
	public InvalidAccessTokenException(String accessToken) {
		super("invalid access token");
	}

	public String getAccessToken() {
		return accessToken;
	}

}
