package com.springsource.greenhouse.badge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.springsource.greenhouse.action.Action;
import com.springsource.greenhouse.action.SimpleAction;

public class StandardBadgeSystem implements BadgeSystem {

	private Map<String, List<ActionTriggeredBadgeAwarder>> simpleActionTriggeredBadgeAwarders;

	private Map<Class<?>, List<ActionTriggeredBadgeAwarder>> actionTriggeredBadgeAwarders;

	public void add(SimpleActionTriggeredBadgeAwarder badgeAwarder, String simpleActionType) {
		
	}
	
	public void add(ActionTriggeredBadgeAwarder<?> badgeAwarder) {
		
	}
	
	public List<AwardedBadge> awardForAction(Action action) {
		if (action instanceof SimpleAction) {
			String simpleActionType = ((SimpleAction)action).getType();
			return invokeAwarders(simpleActionTriggeredBadgeAwarders.get(simpleActionType), action);
		} else {
			return invokeAwarders(actionTriggeredBadgeAwarders.get(action.getClass()), action);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<AwardedBadge> invokeAwarders(List<ActionTriggeredBadgeAwarder> awarders, Action action) {
		if (awarders == null) {
			return null;
		}
		List<AwardedBadge> badges = new ArrayList<AwardedBadge>();
		for (ActionTriggeredBadgeAwarder awarder : awarders) {
			AwardedBadge badge = awarder.awardForAction(action);
			if (badge != null) {
				badges.add(badge);
			}
		}
		return badges.isEmpty() ? null : badges;
	}

}