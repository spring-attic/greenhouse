package com.springsource.greenhouse.events;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;

public class EventSession {
	
	private Integer id;
	
	private String title;
	
	private DateTime startTime;
	
	private DateTime endTime;

	private String description;

	private String hashtag;
	
	private List<EventSessionLeader> leaders;
	
	private Float rating;

	private Boolean favorite;
	
	public EventSession(Integer id, String title, DateTime startTime, DateTime endTime, String description, String hashtag, Float rating, Boolean favorite) {
		this.id = id;
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
		this.hashtag = hashtag;
		this.rating = rating;
		this.favorite = favorite;
	}

	public Integer getId() {
		return id;
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
	
	public List<EventSessionLeader> getLeaders() {
		return Collections.unmodifiableList(leaders);
	}
	
	public void addLeader(EventSessionLeader leader) {
		if (leaders == null) {
			leaders = new LinkedList<EventSessionLeader>();
		}
		leaders.add(leader);
	}
	
	// iphone 1.0.0 compatibility

	// TODO here for compatibility reasons; remove when iphone app is upgraded
	public Short getNumber() {
		return getId().shortValue();
	}

}