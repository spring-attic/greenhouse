package com.springsource.greenhouse.events;

import org.joda.time.LocalDateTime;

public class Event {

	private Long id;

	private String title;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private String location;

	private String description;

	private String name;
	
	private String hashtag;

	private String groupName;
	
	private String groupProfileKey;
	
	public Event(Long id, String title, LocalDateTime startTime, LocalDateTime endTime, String location, String description, String name, String hashtag, String groupName, String groupProfileKey) {
		this.id = id;
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
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

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
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