package com.springsource.greenhouse.events;

import org.joda.time.DateTime;
import org.springframework.social.account.Account;

import com.springsource.greenhouse.action.Action;
import com.springsource.greenhouse.action.Location;

public final class EventTweetAction extends Action {
	
	private final Long eventId;
	
	private final String eventName;
	
	private final Short sessionNumber;
	
	private final String sessionTitle;
	
	private final String tweet;

	public EventTweetAction(Long id, DateTime time, Account account, Location location, Long eventId, String eventName, String tweet) {
		super(id, time, account, location);
		this.eventId = eventId;
		this.eventName = eventName;
		this.tweet = tweet;
		this.sessionNumber = null;
		this.sessionTitle = null;
	}

	public EventTweetAction(Long id, DateTime time, Account account, Location location, Long eventId, String eventName,
			Short sessionNumber, String sessionTitle, String tweet) {
		super(id, time, account, location);
		this.eventId = eventId;
		this.eventName = eventName;
		this.sessionNumber = sessionNumber;
		this.sessionTitle = sessionTitle;
		this.tweet = tweet;		
	}

	public Long getEventId() {
		return eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public Short getSessionNumber() {
		return sessionNumber;
	}

	public String getSessionTitle() {
		return sessionTitle;
	}

	public String getTweet() {
		return tweet;
	}
	

}