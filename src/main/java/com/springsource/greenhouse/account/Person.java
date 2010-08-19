package com.springsource.greenhouse.account;

import org.joda.time.LocalDate;

public final class Person {

	private final String firstName;
	
	private final String lastName;
	
	private final String email;
	
	private final String password;

	private final Gender gender;
	
	private final LocalDate birthdate;
	
	public Person(String firstName, String lastName, String email, String password, Gender gender, LocalDate birthdate) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.birthdate = birthdate;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Gender getGender() {
		return gender;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}	

}
