/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
