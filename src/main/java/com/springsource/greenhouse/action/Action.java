package com.springsource.greenhouse.action;

import org.joda.time.DateTime;

// TODO likely needs a text property
public abstract class Action {
	
	private final Long id;

	private final DateTime time;
	
	private final Long accountId;

	private final Location location;
	
	public Action(Long id, DateTime time, Long accountId, Location location) {
		this.id = id;
		this.time = time;
		this.accountId = accountId;
		this.location = location;
	}

	public Long getId() {
		return id;
	}
	
	public DateTime getTime() {
		return time;
	}

	public Long getAccountId() {
		return accountId;
	}
	
	public Location getLocation() {
		return location;
	}
	
}