package com.springsource.greenhouse.action;

import org.joda.time.DateTime;
import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.utils.Location;

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
		// TODO do not hardcode
		String actionText = "SignedUp".equals(getType()) ? "signed up" : getType();
		return getAccount().getFullName() + " " + actionText;
	}

}