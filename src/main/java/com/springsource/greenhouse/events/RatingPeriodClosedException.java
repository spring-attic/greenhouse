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

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when an attendee attempts to rate a session and the rating period is closed.
 * The rating period opens 10 minutes before the session ends and closes when the event ends.
 * @author Keith Donald
 */
@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public final class RatingPeriodClosedException extends Exception {
	
	private final Long eventId;
	
	private final Integer sessionId;

	public RatingPeriodClosedException(Long eventId, Integer sessionId) {
		super("Session has not yet ended");
		this.eventId = eventId;
		this.sessionId = sessionId;
	}

	/**
	 * The internal id of the event.
	 */
	public Long getEventId() {
		return eventId;
	}

	/**
	 * The internal id of the session.
	 */
	public Integer getSessionId() {
		return sessionId;
	}
	
}