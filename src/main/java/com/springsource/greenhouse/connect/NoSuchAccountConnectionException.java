package com.springsource.greenhouse.connect;

import com.springsource.greenhouse.account.AccountException;

@SuppressWarnings("serial")
public final class NoSuchAccountConnectionException extends AccountException {

	private String accessToken;
	
	public NoSuchAccountConnectionException(String accessToken) {
		super("invalid access token");
	}

	public String getAccessToken() {
		return accessToken;
	}

}
