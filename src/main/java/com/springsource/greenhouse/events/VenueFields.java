package com.springsource.greenhouse.events;

import org.hibernate.validator.constraints.NotEmpty;

public class VenueFields {
	
	private Long id;
	
	@NotEmpty 
	private String name;
	
	@NotEmpty
	private String address; 
	
	private String locationHint;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getLocationHint() {
		return locationHint;
	}
	
	public void setLocationHint(String locationHint) {
		this.locationHint = locationHint;
	}
}
