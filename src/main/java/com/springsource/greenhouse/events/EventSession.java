package com.springsource.greenhouse.events;

import java.util.Date;

public class EventSession {

	private long id;
	
	private long eventId;
	
	private String title;
	
	private String description;
	
	private Date sessionDate;
	
	private Date startTime;
	
	private Date endTime;
	
	private String hashtag;
	
	private long createdByUserId;
	
	private long modifiedByUserId;
	
	private Date lastUpdated;
	
	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getEventId() {
		return this.eventId;
	}
	
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getSessionDate() {
		return this.sessionDate;
	}
	
	public void setSessionDate(Date sessionDate) {
		this.sessionDate = sessionDate;
	}
		
	public Date getStartTime() {
		return this.startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getEndTime() {
		return this.endTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public String getHashtag() {
		return this.hashtag;
	}
	
	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}
	
	public long getCreatedByUserId() {
		return this.createdByUserId;
	}
	
	public void setCreatedByUserId(long createdByUserId) {
		this.createdByUserId = createdByUserId;
	}
	
	public long getModifiedByUserId() {
		return this.modifiedByUserId;
	}
	
	public void setModifiedByUserId(long modifiedByUserId) {
		this.modifiedByUserId = modifiedByUserId;
	}
	
	public Date getLastUpdated() {
		return this.lastUpdated;
	}
	
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
}
