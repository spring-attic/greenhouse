package com.springsource.greenhouse.badge;

import com.springsource.greenhouse.action.Action;

public interface BadgeSystem {
	
	AwardedBadge awardBadgeForAction(Action action);
	
}
