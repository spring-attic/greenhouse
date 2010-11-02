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
package com.springsource.greenhouse.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.springsource.greenhouse.home.UserLocationHandlerInterceptor;

/**
 * A geo-location that can be plotted on a map.
 * @author Keith Donald
 */
public final class Location {
	
	private final Double latitude;
	
	private final Double longitude;

	public Location(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * The lat value.
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * The long value.
	 */
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

	/**
	 * Get the location of the user associated with the current request, if resolvable.
	 */
	public static Location getCurrentLocation() {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		return attributes != null ? (Location) attributes.getAttribute(UserLocationHandlerInterceptor.USER_LOCATION_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST) : null;
	}
	
}
