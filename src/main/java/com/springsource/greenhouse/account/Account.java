package com.springsource.greenhouse.account;

public final class Account {
	
	private final Long id;
	
	private final String firstName;
	
	private final String lastName;
	
	private final String email;
	
	private final String username;
	
	public Account(Long id) {
		this(id, null, null, null, null);
	}
	
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
	
	public String getMemberProfileKey() {
		return username != null ? username : id.toString(); 
	}

	public Account newUsername(String newUsername) {
		return new Account(id, firstName, lastName, email, newUsername);
	}

}