package com.springsource.greenhouse.utils;

public final class Location {
	
	private final Double latitude;
	
	private final Double longitude;

	public Location(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}
	
	public int hashCode() { 
		return latitude.hashCode() + longitude.hashCode() * 29;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Location)) {
			return false;
		}
		Location l = (Location) o;
		return latitude.equals(l.latitude) && l.longitude.equals(l.longitude);
	}
}
