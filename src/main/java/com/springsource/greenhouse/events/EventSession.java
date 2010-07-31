package com.springsource.greenhouse.events;

import java.util.LinkedHashSet;
import java.util.Set;

import org.joda.time.LocalDateTime;

public class EventSession {
	
	private Short number;
	
	private String title;
	
	private LocalDateTime startTime;
	
	private LocalDateTime endTime;

	private String description;

	private String hashtag;
	
	private Set<EventSessionLeader> leaders;
		
	public EventSession(Short number, String title, LocalDateTime startTime, LocalDateTime endTime, String description, String hashtag) {
		this.number = number;
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
		this.hashtag = hashtag;
	}

	public Short getNumber() {
		return number;
	}

	public String getTitle() {
		return title;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public String getDescription() {
		return description;
	}
	
	public String getHashtag() {
		return hashtag;
	}

	public Set<EventSessionLeader> getLeaders() {
		return leaders;
	}

	public void addLeader(EventSessionLeader leader) {
		if (leaders == null) {
			leaders = new LinkedHashSet<EventSessionLeader>();
		}
		leaders.add(leader);
	}

}