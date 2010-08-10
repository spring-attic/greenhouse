package com.springsource.greenhouse.badge;

public interface BadgeRepository {
	
	AwardedBadge createAwardedBadge(String badgeName, Long accountId, Long actionId);
	
}
