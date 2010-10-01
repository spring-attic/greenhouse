package com.springsource.greenhouse.events;

import org.joda.time.DateTime;

public class Event {

	private Long id;

	private String title;

	private DateTime startTime;

	private DateTime endTime;

	private String slug;
	
	private String description;

	private String hashtag;

	private String groupName;
	
	private String groupSlug;
	
	public Event(Long id, String title, DateTime startTime, DateTime endTime, String slug, String description, String hashtag, String groupName, String groupSlug) {
		this.id = id;
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.slug = slug;
		this.description = description;
		this.hashtag = hashtag;
		this.groupName = groupName;
		this.groupSlug = groupSlug;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return this.title;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public String getSlug() {
		return slug;
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

	public String getGroupSlug() {
		return groupSlug;
	}

	// iphone 1.0.0 compatibility
	
	// TODO here for compatibility reasons; remove when iphone app is upgraded
	public String getLocation() {
		return "Chicago, IL";
	}

}