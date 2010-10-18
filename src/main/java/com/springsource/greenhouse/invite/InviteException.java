package com.springsource.greenhouse.invite;

@SuppressWarnings("serial")
public abstract class InviteException extends Exception {

	private final String token;

	public InviteException(String token, String message) {
		super(message);
		this.token = token;
	}

	public String getToken() {
		return token;
	}

}
