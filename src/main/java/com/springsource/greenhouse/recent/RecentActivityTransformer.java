package com.springsource.greenhouse.recent;

import org.springframework.integration.annotation.Transformer;

import com.springsource.greenhouse.action.Action;
import com.springsource.greenhouse.badge.AwardedBadge;

public class RecentActivityTransformer {

	// TODO text should be localized
	
	@Transformer
	public RecentActivity forAction(Action action) {
		String memberPictureUrl = action.getAccount().getProfilePictureUrl();
		// TODO don't hardcode
		String actionImageUrl = "http://localhost:8080/resources/members/action.jpg";
		RecentActivity activity = new RecentActivity(memberPictureUrl, action.toString(), actionImageUrl);
		return activity;
	}
	
	@Transformer
	public RecentActivity forAwardedBadge(AwardedBadge badge) {
		String memberPictureUrl = badge.getAccount().getProfilePictureUrl();
		String text = badge.getAccount().getFullName() + " was awarded the " + badge.getName() + " badge";
		RecentActivity activity = new RecentActivity(memberPictureUrl, text, badge.getImageUrl());
		return activity;
	}
}
