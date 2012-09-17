/*
 * Copyright 2012 the original author or authors.
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
package com.springsource.greenhouse.events.load;

class VenueData {

	private final String name;
	private final  String postalAddress;
	private final  double latitude;
	private final  double longitude;
	private final  String locationHint;

	public VenueData(String name, String postalAddress, double latitude, double longitude, String locationHint) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.postalAddress = postalAddress;
		this.name = name;
		this.locationHint = locationHint;		
	}
	
	public String getName() {
		return name;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getLocationHint() {
		return locationHint;
	}
	
}
