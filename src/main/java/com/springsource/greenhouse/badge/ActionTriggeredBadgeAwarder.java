package com.springsource.greenhouse.badge;

import com.springsource.greenhouse.action.Action;

public interface ActionTriggeredBadgeAwarder<A extends Action> {
	
	public AwardedBadge awardBadgeForAction(A action);
	
}
