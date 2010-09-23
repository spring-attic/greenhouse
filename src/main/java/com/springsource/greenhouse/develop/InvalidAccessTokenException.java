package com.springsource.greenhouse.develop;

import com.springsource.greenhouse.account.AccountException;

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
