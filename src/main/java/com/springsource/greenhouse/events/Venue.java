package com.springsource.greenhouse.events;

import com.springsource.greenhouse.utils.Location;

/**
 * A place where an event is held.
 * @author Keith Donald
 */
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

	/**
	 * The internal id of the Venue.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * The name of the venue as it is known by the public.
	 */
	public String getName() {
		return name;
	}

	/**
	 * The complete postal address of the venue; used for geo-coding the lat/long of the venue.
	 */
	public String getPostalAddress() {
		return postalAddress;
	}
	
	/**
	 * The geo-location of the event.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * A hint about how to find the venue once you are in its general vicinity.
	 * For example, a venue might be "adjacent to the Shopping Center", or at a cross-street.
	 */
	public String getLocationHint() {
		return locationHint;
	}

}
