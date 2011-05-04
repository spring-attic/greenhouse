/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.account;

import org.joda.time.LocalDate;

/**
 * Models a Person who is interested in becoming a member of the application. 
 * @author Keith Donald
 */
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

	/**
	 * The person's first name.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * The person's last name.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * The person's email address, which should be unique among all people.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * The person's desired password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * The person's Gender.
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * The person's birthdate.
	 */
	public LocalDate getBirthdate() {
		return birthdate;
	}	

}