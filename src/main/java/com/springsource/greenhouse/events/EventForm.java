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

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;



import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
/**
 * Model backing the "Register New Event" form.
 * @author UA Team
 */

public class EventForm {

	@NotEmpty
	private String title;

	private DateTimeZone timezone;
	
	private DateTime startTime;
	
	private DateTime endTime;
	
	private String description;
	
	@DateTimeFormat(pattern="M/dd/yy")
	private LocalDate startDate;
	
	@DateTimeFormat(pattern="M/dd/yy")
	private LocalDate endDate;
	
	@NotNull
	private Integer startHour;
	
	@NotNull
	private Integer startMinute;
	
	@NotEmpty
	private String startAmPm;
	
	@NotNull
	private Integer endHour;
	
	@NotNull
	private Integer endMinute;
	
	@NotEmpty
	private String endAmPm;
	
	private Integer venueID;
	@NotEmpty
	private String venueName;
	@NotEmpty
	private String venueAddress;
	
	private String locationHint;
	
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
	public String getTimezone() {
		return timezone != null ? timezone.getID() : "";
	}
	public void setTimezone(String tz) {
		this.timezone = DateTimeZone.forID(tz);
	}
	
	/**
	 * The start time of the conference.
	 */
	public DateTime getStartTime() {
		if (startAmPm.compareTo("AM") == 0) {
		return startTime = new DateTime(startDate.getYear(), startDate.getMonthOfYear(), startDate.getDayOfMonth(), startHour, startMinute, 0, 0, timezone);
		}
		else {
			return startTime = new DateTime(startDate.getYear(), startDate.getMonthOfYear(), startDate.getDayOfMonth(), startHour+12, startMinute, 0, 0, timezone);
		}
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * The end time of the conference.
	 */
	public DateTime getEndTime() {
		if (endAmPm.compareTo("AM") == 0) {
		return endTime  = new DateTime(endDate.getYear(), endDate.getMonthOfYear(), endDate.getDayOfMonth(), endHour, endMinute, 0, 0, timezone);
		}
		else {
			return endTime = new DateTime(endDate.getYear(), endDate.getMonthOfYear(), endDate.getDayOfMonth(), endHour+12, endMinute, 0, 0, timezone);
		}
	}
	
	public void setEndTime(DateTime endTime) {
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
	/**
	 * The StartDate of the event.
	 */
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	/**
	 * The EndDate of the event.
	 */
	public LocalDate getEndDate() {
		return endDate;
	}
	
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	/**
	 * The StartHour for the event.
	 */
	public Integer getStartHour() {
		return startHour;
	}
	
	public void setStartHour(Integer startHour) {
		this.startHour = startHour;
	}
	/**
	 * The StartMinute for the event.
	 */
	public Integer getStartMinute() {
		return startMinute;
	}
	
	public void setStartMinute(Integer startMinute) {
		this.startMinute = startMinute;
	}
	/**
	 * The startAmPm for the event.
	 */
	public String getStartAmPm() {
		return startAmPm;
	}
	
	public void setStartAmPm(String startAmPm) {
		this.startAmPm = startAmPm;
	}
	/**
	 * The endHour for the event.
	 */
	public Integer getEndHour() {
		return endHour;
	}
	
	public void setEndHour(Integer endHour) {
		this.endHour = endHour;
	}
	/**
	 * The endMinute for the event.
	 */
	public Integer getEndMinute() {
		return endMinute;
	}
	
	public void setEndMinute(Integer endMinute) {
		this.endMinute = endMinute;
	}
	/**
	 * The endAmPm for the event.
	 */
	public String getEndAmPm() {
		return endAmPm;
	}
	
	public void setEndAmPm(String endAmPm) {
		this.endAmPm = endAmPm;
	}

	public Integer getVenueID() {
		return venueID;
	}
	public void setVenueID(Integer venueID) {
		this.venueID = venueID;
	}
	public String getVenueName() {
		return venueName;
	}
	public void setVenueName(String venueName) {
		this.venueName = venueName;
	}
	public String getVenueAddress() {
		return venueAddress;
	}
	public void setVenueAddress(String venueAddress) {
		this.venueAddress = venueAddress;
	}
	public String getLocationHint() {
		return locationHint;
	}
	public void setLocationHint(String locationHint) {
		this.locationHint = locationHint;
	}
	
	
}