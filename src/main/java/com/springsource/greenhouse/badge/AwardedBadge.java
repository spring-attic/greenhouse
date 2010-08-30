package com.springsource.greenhouse.badge;

import org.joda.time.DateTime;
import org.springframework.social.account.Account;

import com.springsource.greenhouse.action.Action;

public final class AwardedBadge {
	
	private final Long id;
	
	private final String name;
		
	private final DateTime awardTime;

	private final String imageUrl;
	
	private final Account account;
	
	private final Action action;
	
	public AwardedBadge(Long id, String name, DateTime awardTime, String imageUrl, Account account, Action action) {
		this.id = id;
		this.name = name;
		this.awardTime = awardTime;
		this.imageUrl = imageUrl;
		this.account = account;
		this.action = action;
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

	public String getImageUrl() {
		return imageUrl;
	}
	
	public Account getAccount() {
		return account;
	}

	public Action getAction() {
		return action;
	}
	
}