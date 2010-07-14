package com.springsource.greenhouse.updates;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Update {

	private String text;
	
	private Date timestamp;
	
	private long userId;
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public void setTimestamp(Long milliseconds) {
		this.timestamp = new Date(milliseconds);
	}
	
	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
}
