package com.springsource.greenhouse.recent;

import org.joda.time.DateTime;

public class RecentActivity {

	private final RecentActivityMember member;
	
	private final DateTime time;
	
	private final String icon;
	
	private final String message;

	public RecentActivity(RecentActivityMember member, DateTime time, String icon, String message) {
		this.member = member;
		this.time = time;
		this.icon = icon;
		this.message = message;
	}

	public RecentActivityMember getMember() {
		return member;
	}

	public DateTime getTime() {
		return time;
	}

	public String getIcon() {
		return icon;
	}

	public String getMessage() {
		return message;
	}	
		
}
