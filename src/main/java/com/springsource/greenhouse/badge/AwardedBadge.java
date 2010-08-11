package com.springsource.greenhouse.badge;

import org.joda.time.DateTime;

public final class AwardedBadge {
	
	private final Long id;
	
	private final String name;
		
	private final DateTime awardTime;

	private final Long accountId;
	
	private final Long actionId;
	
	public AwardedBadge(Long id, String name, DateTime awardTime, Long accountId, Long actionId) {
		this.id = id;
		this.name = name;
		this.awardTime = awardTime;
		this.accountId = accountId;
		this.actionId = actionId;
	}

	public Long getId() {
		return id;
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