package com.springsource.greenhouse.account;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Account implements Serializable {
	
	private final Long id;
	
	private final String firstName;
	
	private final String lastName;
	
	private final String email;
	
	private final String username;
	
	// TODO add link to profile picture
	
	public Account(Long id, String firstName, String lastName, String email) {
		this(id, firstName, lastName, email, null);
	}

	public Account(Long id, String firstName, String lastName, String email, String username) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
	}
	
	public Long getId() {
		return id;
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
	
	public String getUsername() {
		return username;
	}
	
	public String getProfileKey() {
		return username != null ? username : id.toString(); 
	}
	
	public String getProfilePictureUrl() {
		// TODO populate
		return null;
	}
	
	public Account newUsername(String newUsername) {
		return new Account(id, firstName, lastName, email, newUsername);
	}

}