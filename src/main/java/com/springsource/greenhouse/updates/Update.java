package com.springsource.greenhouse.updates;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Update {

	private String text;
	
	private String timestamp;
	
	private long userId;
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public void setTimestamp(Long milliseconds) {
		Date date = new Date(milliseconds);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd 'at' hh:mm:ss z");
		this.timestamp = formatter.format(date);
	}
	
	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
}
