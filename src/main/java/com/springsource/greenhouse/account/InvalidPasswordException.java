package com.springsource.greenhouse.account;

@SuppressWarnings("serial")
public class InvalidPasswordException extends Exception {

	public InvalidPasswordException() {
		super("Invalid password");
	}
}
