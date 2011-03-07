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
public class EventsForm {
	
	@NotEmpty
	private String title;
	
	
	private String timezone;
	
	
	private Date startTime;

	
	private Date endTime;

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
	public String getTimeZone() {
		return timezone;
	}

	public void setTimeZone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * The start time of the conference.
	 */
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * The end time of the conference.
	 */
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
