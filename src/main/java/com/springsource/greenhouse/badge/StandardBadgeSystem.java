package com.springsource.greenhouse.badge;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.GenericTypeResolver;

import com.springsource.greenhouse.action.Action;
import com.springsource.greenhouse.action.SimpleAction;

class StandardBadgeSystem implements BadgeSystem {

	private Map<String, SimpleActionTriggeredBadgeAwarder> simpleActionTriggeredBadgeAwarders = 
		new HashMap<String, SimpleActionTriggeredBadgeAwarder>();

	private Map<Class<? extends Action>, ActionTriggeredBadgeAwarder<? extends Action>> actionTriggeredBadgeAwarders = 
		new HashMap<Class<? extends Action>, ActionTriggeredBadgeAwarder<? extends Action>>();

	public void add(SimpleActionTriggeredBadgeAwarder badgeAwarder, String simpleActionType) {
		simpleActionTriggeredBadgeAwarders.put(simpleActionType, badgeAwarder);
	}
	
	public void add(ActionTriggeredBadgeAwarder<? extends Action> badgeAwarder) {
		@SuppressWarnings("unchecked")
		Class<? extends Action> actionType = (Class<? extends Action>) GenericTypeResolver.resolveTypeArgument(badgeAwarder.getClass(), ActionTriggeredBadgeAwarder.class);
		actionTriggeredBadgeAwarders.put(actionType, badgeAwarder);		
	}
	
	public AwardedBadge awardBadgeForAction(Action action) {
		if (action instanceof SimpleAction) {
			String simpleActionType = ((SimpleAction)action).getType();
			return invokeBadgeAwarder(simpleActionTriggeredBadgeAwarders.get(simpleActionType), action);
		} else {
			return invokeBadgeAwarder(actionTriggeredBadgeAwarders.get(action.getClass()), action);
		}
	}

	@SuppressWarnings("unchecked")
	private AwardedBadge invokeBadgeAwarder(ActionTriggeredBadgeAwarder awarder, Action action) {
		if (awarder == null) {
			return null;
		}
		return awarder.awardBadgeForAction(action);
	}

}