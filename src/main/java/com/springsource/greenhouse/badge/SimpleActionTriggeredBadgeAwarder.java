package com.springsource.greenhouse.badge;

import com.springsource.greenhouse.action.SimpleAction;

public class SimpleActionTriggeredBadgeAwarder implements ActionTriggeredBadgeAwarder<SimpleAction> {

	private String badgeName;
	
	private BadgeRepository badgeRepository;
	
	public SimpleActionTriggeredBadgeAwarder(String badgeName, BadgeRepository badgeRepository) {
		this.badgeName = badgeName;
		this.badgeRepository = badgeRepository;
	}

	public AwardedBadge awardBadgeForAction(SimpleAction action) {
		return badgeRepository.createAwardedBadge(badgeName, action.getAccount(), action);
	}

}
