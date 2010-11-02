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

/**
 * A value object representing a session rating entered by an attendee.
 * @author Keith Donald
 */
public final class Rating {
	
	private final Short value;
	
	private final String comment;

	public Rating(Short value, String comment) {
		if (value <= 0 || value > 5) {
			throw new IllegalArgumentException("Rating value must be between 1 and 5");
		}
		this.value = value;
		this.comment = comment;
	}

	/**
	 * The rating value between 1 and 5.
	 * 1 = Poor
	 * 2 = Needs Development
	 * 3 = Average
	 * 4 = Good
	 * 5 = Excellent
	 */
	public Short getValue() {
		return value;
	}

	/**
	 * A comment explaining the rating for the benefit of the session leaders.
	 */
	public String getComment() {
		return comment;
	}
	
}