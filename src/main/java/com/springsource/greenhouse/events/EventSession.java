package com.springsource.greenhouse.events;

import java.util.LinkedHashSet;
import java.util.Set;

import org.joda.time.DateTime;

public class EventSession {
	
	private Short code;
	
	private String title;
	
	private DateTime startTime;
	
	private DateTime endTime;

	private String description;

	private Set<EventSessionLeader> leaders;
		
	public EventSession(Short code, String title, DateTime startTime, DateTime endTime, String description) {
		this.code = code;
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Short getCode() {
		return code;
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