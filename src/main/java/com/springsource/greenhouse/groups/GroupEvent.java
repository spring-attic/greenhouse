package com.springsource.greenhouse.groups;

import com.springsource.greenhouse.events.Event;

public class GroupEvent {
	
	private String searchString;
	
	private Event event;
	
	public GroupEvent(String searchString, Event event) {
		this.searchString = searchString;
		this.event = event;
	}

	public Event getEvent() {
		return event;
	}
	
	public String getSearchString() {
		return searchString;
	}
	
}
