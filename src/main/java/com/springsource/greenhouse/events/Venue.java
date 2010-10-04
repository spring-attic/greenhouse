package com.springsource.greenhouse.events;

import com.springsource.greenhouse.utils.Location;

public class Venue {
	
	private final Long id;
	
	private final String name;

	private final String postalAddress;
	
	private final Location location;

	private final String locationHint;
	
	public Venue(Long id, String name, String postalAddress, Location location, String locationHint) {
		this.id = id;
		this.name = name;
		this.postalAddress = postalAddress;
		this.location = location;
		this.locationHint = locationHint;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPostalAddress() {
		return postalAddress;
	}
	
	public Location getLocation() {
		return location;
	}

	public String getLocationHint() {
		return locationHint;
	}

}
