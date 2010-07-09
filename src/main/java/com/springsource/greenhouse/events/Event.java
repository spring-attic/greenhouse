package com.springsource.greenhouse.events;

import java.util.Date;
import java.util.List;

public class Event {
	
	private long id;
	
	private String title;
	
	private String description;
	
	private Date startDate;
	
	private Date endDate;
	
	private Date startTime;
	
	private Date endTime;
	
	private String hashtag;
	
	private List<EventSession> sessions;
	
	private long createdByUserId;
	
	private long modifiedByUserId;
	
	private Date lastUpdated;
	
	public long getId() {
		return this.id;
	}
	
	public void setId(long id) {
		this.id = id;
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
	
	public Date getStartDate() {
		return this.startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return this.endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	
	public List<EventSession> getSessions() {
		return this.sessions;
	}
	
	public void setSessions(List<EventSession> sessions) {
		this.sessions = sessions;
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
