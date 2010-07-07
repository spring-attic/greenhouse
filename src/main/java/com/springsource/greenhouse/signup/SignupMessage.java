package com.springsource.greenhouse.signup;

public class SignupMessage {
	
	private final Long userId;
	
	private final String firstName;
	
	private final String lastName;
	
	private final String email;
	
	public SignupMessage(Long userId, String firstName, String lastName, String email) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public Long getUserId() {
		return userId;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getProfileKey() {
		return String.valueOf(userId);
	}

}