package com.springsource.greenhouse.events;

import org.joda.time.DateTime;

import com.springsource.greenhouse.action.Action;
import com.springsource.greenhouse.action.Location;

public final class EventTweetAction extends Action {
	
	private final Long eventId;
	
	private final Short sessionNumber;
	
	private final String tweet;

	public EventTweetAction(Long id, DateTime time, Long accountId, Location location,
			Long eventId, Short sessionNumber, String tweet) {
		super(id, time, accountId, location);
		this.eventId = eventId;
		this.sessionNumber = sessionNumber;
		this.tweet = tweet;
	}

	public Long getEventId() {
		return eventId;
	}

	public Short getSessionNumber() {
		return sessionNumber;
	}

	public String getTweet() {
		return tweet;
	}

}