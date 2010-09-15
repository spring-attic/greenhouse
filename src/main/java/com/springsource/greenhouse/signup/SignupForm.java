package com.springsource.greenhouse.signup;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.joda.time.LocalDate;

import com.springsource.greenhouse.account.Gender;
import com.springsource.greenhouse.account.Person;

public class SignupForm {
	
	@NotNull
	private String firstName;

	@NotNull
	private String lastName;

	@NotNull
	@Size(max=320)
	private String email;

	@NotNull
	@Size(min=6)
	// TODO confirmation constraint	
	private String password;

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
	
	public Person createPerson() {
		// TODO add gender and birthdate
		return new Person(firstName, lastName, email, password, Gender.Male, new LocalDate(1977, 12, 1));
	}
}