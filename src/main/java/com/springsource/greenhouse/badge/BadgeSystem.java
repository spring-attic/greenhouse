package com.springsource.greenhouse.badge;

import java.util.List;

import com.springsource.greenhouse.action.Action;

public interface BadgeSystem {
	
	List<AwardedBadge> awardForAction(Action action);
	
}
