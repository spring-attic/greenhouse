package com.springsource.greenhouse.events;

public class EventSessionLeader {

	private String name;
	
	public EventSessionLeader(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	// TODO remains for iPhone Client Compatibility	
	public String getFirstName() {
		return name.split(" ")[0];
	}

	// TODO remains for iPhone Client Compatibility	
	public String getLastName() {
		return name.split(" ")[1];
	}

}
