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
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Model backing the "Create New Event" form.
 * @author UA Team
 */
public class EventForm {

	@NotEmpty
	private String title;

	private DateTimeZone timeZone;
	
	private DateFields startTimeFields = new DateFields();
	
	private DateFields endTimeFields = new DateFields();

	private VenueFields venueFields = new VenueFields();

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
	 * The time zone the event takes place in.
	 */
	public String getTimeZone() {
		return timeZone != null ? timeZone.getID() : "";
	}
	
	public void setTimeZone(String timeZone) {
		this.timeZone = DateTimeZone.forID(timeZone);
	}

	/**
	 * Fields to complete to enter the time the event starts.
	 * @see #getStartTime()
	 */
	public DateFields getStartTimeFields() {
		return startTimeFields;
	}

	/**
	 * Fields to complete to enter the time the event ends.
	 * @see #getEndTime()
	 */
	public DateFields getEndTimeFields() {
		return endTimeFields;
	}
	
	/**
	 * The start time of the event, calculated from the completed field values.
	 */
	public DateTime getStartTime() {
		return startTimeFields.getDateTime(timeZone);
	}

	/**
	 * The end time of the event, calculated from the completed field values.
	 */
	public DateTime getEndTime() {
		return endTimeFields.getDateTime(timeZone);
	}

	/**
	 * Fields to complete to indicate where the event is held.
	 */
	public VenueFields getVenueFields() {
		return venueFields;
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