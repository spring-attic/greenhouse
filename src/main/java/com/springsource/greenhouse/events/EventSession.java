package com.springsource.greenhouse.events;

import java.util.LinkedHashSet;
import java.util.Set;

import org.joda.time.DateTime;

public class EventSession {
	
	private Short number;
	
	private String title;
	
	private DateTime startTime;
	
	private DateTime endTime;

	private String description;

	private String hashtag;
	
	private Set<EventSessionLeader> leaders;
	
	private Float rating;

	private Boolean favorite;
	
	public EventSession(Short number, String title, DateTime startTime, DateTime endTime, String description, String hashtag, Float rating, Boolean favorite) {
		this.number = number;
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
		this.hashtag = hashtag;
		this.rating = rating;
		this.favorite = favorite;
	}

	public Short getNumber() {
		return number;
	}

	public String getTitle() {
		return title;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public String getDescription() {
		return description;
	}
	
	public String getHashtag() {
		return hashtag;
	}

	public Float getRating() {
		return rating;
	}

	public boolean isFavorite() {
		return favorite;
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