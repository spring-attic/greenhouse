package com.springsource.greenhouse.account;

@SuppressWarnings("serial")
public class ProfilePictureException extends Exception {
	public ProfilePictureException(String message) {
		super(message);
	}
	
	public ProfilePictureException(String message, Throwable t) {
		super(message, t);
	}
}
