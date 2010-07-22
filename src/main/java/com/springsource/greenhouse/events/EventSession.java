package com.springsource.greenhouse.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventSession {
	
	private String title;
	
	private String summary;
	
	private Date startTime;
	
	private Date endTime;

	private List<SessionLeader> leaders;

	public EventSession(String title, String summary, Date startTime,
			Date endTime, SessionLeader leader) {
		super();
		this.title = title;
		this.summary = summary;
		this.startTime = startTime;
		this.endTime = endTime;
		
		this.leaders = new ArrayList<SessionLeader>();
		leaders.add(leader);
	}

	public EventSession(String title, String summary, Date startTime,
			Date endTime, List<SessionLeader> leaders) {
		super();
		this.title = title;
		this.summary = summary;
		this.startTime = startTime;
		this.endTime = endTime;
		this.leaders = leaders;
	}
	
	public String getTitle() {
		return title;
	}

	public String getSummary() {
		return summary;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public List<SessionLeader> getLeaders() {
		return leaders;
	}

}
