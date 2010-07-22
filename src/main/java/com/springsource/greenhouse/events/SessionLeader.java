package com.springsource.greenhouse.events;

public class SessionLeader {

	private String firstName;
	
	private String lastName;

	public SessionLeader(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
}
