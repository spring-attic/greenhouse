package com.springsource.greenhouse.events;

import org.joda.time.DateTime;

public class Event {

	private Long id;

	private String title;

	private DateTime startDate;

	private DateTime endDate;

	private String location;

	private String description;

	private String name;
	
	private String hashtag;

	private String groupName;
	
	private String groupProfileKey;
	
	public Event(Long id, String title, DateTime startDate, DateTime endDate, String location, String description, String name, String hashtag, String groupName, String groupProfileKey) {
		this.id = id;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.location = location;
		this.description = description;
		this.name = name;
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

	public DateTime getStartDate() {
		return startDate;
	}

	public DateTime getEndDate() {
		return endDate;
	}

	public String getLocation() {
		return location;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
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