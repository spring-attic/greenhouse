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
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
/**
 * Model backing the "Register New Event" form.
 * @author Matt Downs
 */
public class EventForm {

	@NotEmpty
	private String title;

	private DateTimeZone timezone;
	
	private DateTime startTime;
	
	private DateTime endTime;
	@NotEmpty
	private String description;
	
	@DateTimeFormat(pattern="M/dd/yy")
	private LocalDate startDate;
	
	@DateTimeFormat(pattern="M/dd/yy")
	private LocalDate endDate;
	
	private Integer startHour;
	
	private Integer startMinute;
	
	private String startAmPm;
	
	private Integer endHour;
	
	private Integer endMinute;
	
	private String endAmPm;
	
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
	 * @return the startDate
	 */
	public LocalDate getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public LocalDate getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the startHour
	 */
	public Integer getStartHour() {
		return startHour;
	}
	/**
	 * @param startHour the startHour to set
	 */
	public void setStartHour(Integer startHour) {
		this.startHour = startHour;
	}
	/**
	 * @return the startMinute
	 */
	public Integer getStartMinute() {
		return startMinute;
	}
	/**
	 * @param startMinute the startMinute to set
	 */
	public void setStartMinute(Integer startMinute) {
		this.startMinute = startMinute;
	}
	/**
	 * @return the startAmPm
	 */
	public String getStartAmPm() {
		return startAmPm;
	}
	/**
	 * @param startAmPm the startAmPm to set
	 */
	public void setStartAmPm(String startAmPm) {
		this.startAmPm = startAmPm;
	}
	/**
	 * @return the endHour
	 */
	public Integer getEndHour() {
		return endHour;
	}
	/**
	 * @param endHour the endHour to set
	 */
	public void setEndHour(Integer endHour) {
		this.endHour = endHour;
	}
	/**
	 * @return the endMinute
	 */
	public Integer getEndMinute() {
		return endMinute;
	}
	/**
	 * @param endMinute the endMinute to set
	 */
	public void setEndMinute(Integer endMinute) {
		this.endMinute = endMinute;
	}
	/**
	 * @return the endAmPm
	 */
	public String getEndAmPm() {
		return endAmPm;
	}
	/**
	 * @param endAmPm the endAmPm to set
	 */
	public void setEndAmPm(String endAmPm) {
		this.endAmPm = endAmPm;
	}

}