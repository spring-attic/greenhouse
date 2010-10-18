package com.springsource.greenhouse.invite;

@SuppressWarnings("serial")
public final class InviteAlreadyAcceptedException extends InviteException {
	
	public InviteAlreadyAcceptedException(String token) {
		super(token, "Invite already accepted with token '" + token + "'");
	}
	
}
