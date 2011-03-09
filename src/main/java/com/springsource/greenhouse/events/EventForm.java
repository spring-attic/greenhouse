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

import java.util.Date;

import org.hibernate.validator.constraints.NotEmpty;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Model backing the "Register New Event" form.
 * @author Matt Downs
 */
public class EventForm {
	
	@NotEmpty
	private String title;
	
	
	private DateTimeZone timezone;
	
	@NotEmpty
	private String startTime;

	@NotEmpty
	private String endTime;

	@NotEmpty
	private String description;
	
	/**
	 * The title of the event.
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * The time zone the conference takes place in.
	 */
	public DateTimeZone getTimezone() {
		return timezone;
	}

	public void setTimezone(DateTimeZone timezone) {
		this.timezone = timezone;
	}

	/**
	 * The start time of the conference.
	 */
	public DateTime getStartTime() {
		return new DateTime(startTime);
	}



	public void setStartTime(DateTime startTime) {
		this.startTime = startTime.toString();
	}

	/**
	 * The end time of the conference.
	 */
	public DateTime getEndTime() {
		return new DateTime(endTime);
	}

	public void setEndTime(DateTime endTime) {
		this.endTime = endTime.toString();
	}
	
	/**
	 * A short description of the event.
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
