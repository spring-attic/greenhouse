package com.springsource.greenhouse.signup.mail;

public class SignupMessage {
	private final int userId;
	private final String email;
	private final String firstName;
	
	public SignupMessage(int userId, String email, String firstName) {
		this.userId = userId;
		this.email = email;
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public int getUserId() {
		return userId;
	}
}
