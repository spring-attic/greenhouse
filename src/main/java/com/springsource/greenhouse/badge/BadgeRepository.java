package com.springsource.greenhouse.badge;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.action.Action;

public interface BadgeRepository {
	
	AwardedBadge createAwardedBadge(String badge, Account account, Action action);
	
}
