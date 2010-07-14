package com.springsource.greenhouse.account;

@SuppressWarnings("serial")
public class InvalidPasswordException extends AccountException {

	public InvalidPasswordException() {
		super("invalid password");
	}
}
