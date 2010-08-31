package com.springsource.greenhouse.action;

import org.joda.time.DateTime;
import org.springframework.social.account.Account;

public abstract class Action {
	
	private final Long id;

	private final DateTime time;
	
	private final Account account;
	
	private final Location location;
	
	public Action(Long id, DateTime time, Account account, Location location) {
		this.id = id;
		this.time = time;
		this.account = account;
		this.location = location;
	}

	public Long getId() {
		return id;
	}
	
	public DateTime getTime() {
		return time;
	}

	public Account getAccount() {
		return account;
	}
	
	public Location getLocation() {
		return location;
	}
	
}