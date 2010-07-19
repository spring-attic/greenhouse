package com.springsource.greenhouse.events;

import java.util.Date;

public class Event {

	private long id;

	private String friendlyId;
	
	private String title;

	private String location;

	private String description;

	private Date startTime;

	private Date endTime;

	private String hashtag;

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

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public void setFriendlyId(String friendlyId) {
	    this.friendlyId = friendlyId;
    }

	public String getFriendlyId() {
	    return friendlyId;
    }
}
