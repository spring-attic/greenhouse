package com.springsource.greenhouse.events;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value=HttpStatus.PRECONDITION_FAILED)
public final class SessionNotEndedException extends Exception {
	
	private final Long eventId;
	
	private final Integer sessionId;

	public SessionNotEndedException(Long eventId, Integer sessionId) {
		super("Session has not yet ended");
		this.eventId = eventId;
		this.sessionId = sessionId;
	}

	public Long getEventId() {
		return eventId;
	}

	public Integer getSessionId() {
		return sessionId;
	}
	
}