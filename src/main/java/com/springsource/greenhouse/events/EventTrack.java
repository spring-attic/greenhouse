package com.springsource.greenhouse.events;

import com.springsource.greenhouse.members.Member;

public class EventTrack {
	public long getIdentity() {
		return identity;
	}

	public void setIdentity(long identity) {
		this.identity = identity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Member getChair() {
		return chair;
	}

	public void setChair(Member chair) {
		this.chair = chair;
	}

	private long identity;
	private String name;
	private String description;
	private Member chair;
}
