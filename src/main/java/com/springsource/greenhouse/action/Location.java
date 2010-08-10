package com.springsource.greenhouse.action;

public final class Location {
	
	private final Float latitude;
	
	private final Float longitude;

	public Location(Float latitude, Float longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Float getLatitude() {
		return latitude;
	}

	public Float getLongitude() {
		return longitude;
	}
	
}
