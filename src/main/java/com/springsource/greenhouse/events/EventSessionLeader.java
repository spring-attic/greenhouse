package com.springsource.greenhouse.events;

// TODO - consider factoring out a MemberReferencce or AccountReference class with firstName/lastName and profileKey
public class EventSessionLeader {

	private String firstName;
	
	private String lastName;

	public EventSessionLeader(String firstName, String lastName) {
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
