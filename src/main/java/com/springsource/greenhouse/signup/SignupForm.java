package com.springsource.greenhouse.signup;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.springsource.greenhouse.account.Person;

public class SignupForm {
	
	@NotEmpty
	private String firstName;

	@NotEmpty
	private String lastName;

	@NotEmpty
	@Size(max=320)
	private String email;

	@NotEmpty
	@Size(min=6)
	private String password = "";

	@NotEmpty
	@Size(min=6)
	private String confirmPassword = "";

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
	
	@AssertTrue(message="could not confirm")
	public boolean isPasswordConfirmed() {
		return password.equals(confirmPassword);
	}

	public Person createPerson() {
		return new Person(firstName, lastName, email, password);
	}
}