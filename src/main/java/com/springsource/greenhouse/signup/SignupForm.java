package com.springsource.greenhouse.signup;

import javax.validation.constraints.Size;

public class SignupForm {
	
	@Size(min=1)
	private String firstName;

	@Size(min=1)
	private String lastName;

	@Size(min=1, max=320)
	private String email;

	@Size(min=6)
	private String password;

	@Size(min=6)
	private String confirmPassword;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}