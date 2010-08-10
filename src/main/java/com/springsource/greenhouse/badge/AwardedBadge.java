package com.springsource.greenhouse.badge;

import org.joda.time.DateTime;

public final class AwardedBadge {
	
	private final String name;
		
	private final DateTime awardTime;

	private final Long accountId;
	
	private final Long actionId;
	
	public AwardedBadge(String name, DateTime awardTime, Long accountId, Long actionId) {
		this.name = name;
		this.awardTime = awardTime;
		this.accountId = accountId;
		this.actionId = actionId;
	}

	public String getName() {
		return name;
	}

	public DateTime getAwardTime() {
		return awardTime;
	}

	public Long getAccountId() {
		return accountId;
	}

	public Long getActionId() {
		return actionId;
	}
	
}