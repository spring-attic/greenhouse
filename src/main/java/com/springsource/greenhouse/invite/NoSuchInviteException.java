package com.springsource.greenhouse.invite;

@SuppressWarnings("serial")
public final class NoSuchInviteException extends InviteException {

	public NoSuchInviteException(String token) {
		super(token, "No such invite with token '" + token + "'");
	}

}
