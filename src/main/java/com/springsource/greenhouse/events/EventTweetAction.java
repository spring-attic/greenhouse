/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.events;

import org.joda.time.DateTime;
import com.springsource.greenhouse.account.Account;

import com.springsource.greenhouse.activity.action.Action;
import com.springsource.greenhouse.utils.Location;

/**
 * A record of a member tweeting about an event or a specific session at a event.
 * TODO: this class is not yet used
 * @author Keith Donald
 */
public final class EventTweetAction extends Action {
	
	private final Long eventId;
	
	private final String eventTitle;
	
	private final Short sessionNumber;
	
	private final String sessionTitle;
	
	private final String tweet;

	/**
	 * Create a EventTweetAction that records a general event tweet posted by an attendee.
	 */
	public EventTweetAction(Long id, DateTime time, Account account, Location location, Long eventId, String eventTitle, String tweet) {
		super(id, time, account, location);
		this.eventId = eventId;
		this.eventTitle = eventTitle;
		this.tweet = tweet;
		this.sessionNumber = null;
		this.sessionTitle = null;
	}

	/**
	 * Create a EventTweetAction that records a specific session tweet posted by an attendee.
	 */
	public EventTweetAction(Long id, DateTime time, Account account, Location location, Long eventId, String eventTitle, Short sessionNumber, String sessionTitle, String tweet) {
		super(id, time, account, location);
		this.eventId = eventId;
		this.eventTitle = eventTitle;
		this.sessionNumber = sessionNumber;
		this.sessionTitle = sessionTitle;
		this.tweet = tweet;		
	}

	/**
	 * The internal id of the event.
	 */
	public Long getEventId() {
		return eventId;
	}

	/**
	 * A short, descriptive name of the event.
	 */
	public String getEventTitle() {
		return eventTitle;
	}

	/**
	 * The internal id of the session the tweet was about.
	 * Null if this is a general event tweet.
	 */
	public Short getSessionNumber() {
		return sessionNumber;
	}

	/**
	 * The title of the session this tweet was about.
	 * Null if this is a general event tweet.
	 */
	public String getSessionTitle() {
		return sessionTitle;
	}

	/**
	 * The text of the tweet posted by the attendee.
	 */
	public String getTweet() {
		return tweet;
	}

}