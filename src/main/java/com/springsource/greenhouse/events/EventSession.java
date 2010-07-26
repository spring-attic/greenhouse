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
	
	private String hashtag;

	public EventSession(String title, String summary, Date startTime,
			Date endTime, SessionLeader leader, String hashtag) {
		super();
		this.title = title;
		this.summary = summary;
		this.startTime = startTime;
		this.endTime = endTime;
		
		this.leaders = new ArrayList<SessionLeader>();
		this.leaders.add(leader);
		this.hashtag = hashtag;
	}

	public EventSession(String title, String summary, Date startTime,
			Date endTime, List<SessionLeader> leaders, String hashtag) {
		super();
		this.title = title;
		this.summary = summary;
		this.startTime = startTime;
		this.endTime = endTime;
		this.leaders = leaders;
		this.hashtag = hashtag;
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

	public String getHashtag() {
		return hashtag;
	}
}
