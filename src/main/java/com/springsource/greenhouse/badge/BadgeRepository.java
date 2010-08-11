package com.springsource.greenhouse.badge;

public interface BadgeRepository {
	
	AwardedBadge createAwardedBadge(String badge, Long accountId, Long actionId);
	
}
