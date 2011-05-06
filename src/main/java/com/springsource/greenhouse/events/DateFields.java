package com.springsource.greenhouse.events;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class DateFields {

	@DateTimeFormat(pattern="M/dd/yy")
	private LocalDate day;
	
	@NotNull
	private Integer hour;
	
	@NotNull
	private Integer minute;
	
	@NotEmpty
	private String halfDay;

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public Integer getMinute() {
		return minute;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}

	public String getHalfDay() {
		return halfDay;
	}

	public void setHalfDay(String halfDay) {
		this.halfDay = halfDay;
	}

	public DateTime getDateTime(DateTimeZone timeZone) {
		return new DateTime(day.getYear(), day.getMonthOfYear(), day.getDayOfMonth(), hour, minute, 0, 0, timeZone).withField(DateTimeFieldType.halfdayOfDay(), halfDayFieldValue());
	}
	
	// internal helpers;
	
	private int halfDayFieldValue() {
		return halfDay.equalsIgnoreCase("PM") ? 1 : 0;
	}
	
}