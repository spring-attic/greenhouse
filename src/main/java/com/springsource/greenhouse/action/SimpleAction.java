package com.springsource.greenhouse.action;

import org.joda.time.DateTime;

public final class SimpleAction extends Action {
	
	private final String type;

	public SimpleAction(String type, Long id, DateTime time, Long accountId, Location location) {
		super(id, time, accountId, location);
		this.type = type;
	}

	public String getType() {
		return type;
	}

}