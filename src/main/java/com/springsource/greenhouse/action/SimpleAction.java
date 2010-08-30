package com.springsource.greenhouse.action;

import org.joda.time.DateTime;
import org.springframework.social.account.Account;

public final class SimpleAction extends Action {
	
	private final String type;

	public SimpleAction(String type, Long id, DateTime time, Account account, Location location) {
		super(id, time, account, location);
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public String toString() {
		return getAccount().getFullName() + " " + getType();
	}

}