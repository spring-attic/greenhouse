package com.springsource.greenhouse.events;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class EventSession {
	
	private Short code;
	
	private String title;
	
	private Date startTime;
	
	private Date endTime;

	private String description;
	
	private Set<EventSessionLeader> leaders;
		
	public EventSession(Short code, String title, Date startTime, Date endTime, String description) {
		this.code = code;
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
	}

	public Short getCode() {
		return code;
	}

	public String getTitle() {
		return title;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
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