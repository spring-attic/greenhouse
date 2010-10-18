package com.springsource.greenhouse.recent;

import org.springframework.integration.annotation.Transformer;

import com.springsource.greenhouse.action.Action;
import com.springsource.greenhouse.badge.AwardedBadge;

public class RecentActivityTransformer {

	// TODO text should be localized
	
	@Transformer
	public RecentActivity forAction(Action action) {
		String memberPictureUrl = action.getAccount().getPictureUrl();
		// TODO don't hardcode
		String imageUrl = "http://images.greenhouse.springsource.org/activity/icon-default-action.png";
		RecentActivity activity = new RecentActivity(memberPictureUrl, action.toString(), imageUrl);
		return activity;
	}
	
	@Transformer
	public RecentActivity forAwardedBadge(AwardedBadge badge) {
		String memberPictureUrl = badge.getAccount().getPictureUrl();
		String text = badge.getAccount().getFullName() + " was awarded the " + badge.getName() + " badge";
		RecentActivity activity = new RecentActivity(memberPictureUrl, text, badge.getImageUrl());
		return activity;
	}
}
