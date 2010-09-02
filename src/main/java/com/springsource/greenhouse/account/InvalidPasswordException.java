package com.springsource.greenhouse.account;

@SuppressWarnings("serial")
public final class InvalidPasswordException extends AccountException {

	public InvalidPasswordException() {
		super("invalid password");
	}
}
