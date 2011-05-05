package com.springsource.greenhouse.events;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class EventSessionForm {
	
	@NotEmpty
	private String title;
	
    private DateTime startTime;
	
	private DateTime endTime;
	
	private String hashtag;
	
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
	
	private Integer leaderID;
	
	private String name;
	
	private String company;
	
	private String companyTitle;
	
	private String companyURL;
	
	private String twitterName;
	
	@NotEmpty
	private String trackCode;
		
 
	/**
	 * The title of the event.
	 */
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	/**
	 * The start time of the conference.
	 */
	public DateTime getStartTime() {
		if (startAmPm.compareTo("AM") == 0) {
		return startTime = new DateTime(startDate.getYear(), startDate.getMonthOfYear(), startDate.getDayOfMonth(), startHour, startMinute, 0, 0);
		}
		else {
			return startTime = new DateTime(startDate.getYear(), startDate.getMonthOfYear(), startDate.getDayOfMonth(), startHour+12, startMinute, 0, 0);
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
		return endTime  = new DateTime(endDate.getYear(), endDate.getMonthOfYear(), endDate.getDayOfMonth(), endHour, endMinute, 0, 0);
		}
		else {
			return endTime = new DateTime(endDate.getYear(), endDate.getMonthOfYear(), endDate.getDayOfMonth(), endHour+12, endMinute, 0, 0);
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
	
	public String getHashtag() {
		return hashtag;
	}
	
	public void setHashtag(String hashtag) {
	this.hashtag = hashtag;
	}
	public Integer getLeaderID() {
		return leaderID;
	}
	public void setLeaderID(Integer leaderID) {
		this.leaderID = leaderID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompanyTitle() {
		return companyTitle;
	}
	public void setCompanyTitle(String companyTitle) {
		this.companyTitle = companyTitle;
	}
	public String getCompanyURL() {
		return companyURL;
	}
	public void setCompanyURL(String companyURL) {
		this.companyURL = companyURL;
	}
	public String getTwitterName() {
		return twitterName;
	}
	public void setTwitterName(String twitterName) {
		this.twitterName = twitterName;
	}
	public String getTrackCode() {
		return trackCode;
	}
	public void setTrackCode(String trackCode) {
		this.trackCode = trackCode;
	}
	
	
	
}
