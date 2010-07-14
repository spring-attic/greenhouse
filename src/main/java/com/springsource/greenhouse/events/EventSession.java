package com.springsource.greenhouse.events;

import java.util.Date;

import com.springsource.greenhouse.members.Member;

public class EventSession {

	private String code;
	
	private String title;
	
	private String description;

	private Date startTime;
	
	private Date endTime;
	
	private String hashtag;

	private Member speaker;
		
	public String getCode() {
		return this.code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
		
	public Date getStartTime() {
		return this.startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getEndTime() {
		return this.endTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public String getHashtag() {
		return this.hashtag;
	}
	
	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	public void setSpeaker(Member speaker) {
		this.speaker = speaker;
    }
	
	public Member getSpeaker() {
		return this.speaker;
	}
}
