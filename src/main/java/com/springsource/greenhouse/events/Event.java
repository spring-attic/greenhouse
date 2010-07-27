package com.springsource.greenhouse.events;

import java.util.Date;

public class Event {

	private Long id;

	private String title;

	private Date startDate;

	private Date endDate;

	private String location;

	private String description;
	
	private String hashtag;

	private String groupName;
	
	private String groupProfileKey;
	
	public Event(Long id, String title, Date startDate, Date endDate, String location, String description, String hashtag, String groupName, String groupProfileKey) {
		this.id = id;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.location = location;
		this.description = description;
		this.hashtag = hashtag;
		this.groupName = groupName;
		this.groupProfileKey = groupProfileKey;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return this.title;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getLocation() {
		return location;
	}

	public String getDescription() {
		return description;
	}

	public String getHashtag() {
		return hashtag;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getGroupProfileKey() {
		return groupProfileKey;
	}

}