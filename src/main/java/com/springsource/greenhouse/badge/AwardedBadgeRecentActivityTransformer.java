package com.springsource.greenhouse.badge;

import org.springframework.integration.annotation.Transformer;

import com.springsource.greenhouse.recent.RecentActivity;

public class AwardedBadgeRecentActivityTransformer {

	@Transformer
	public RecentActivity forAwardedBadge(AwardedBadge badge) {
		String message = badge.getAccount().getFullName() + " was awarded the " + badge.getName() + " badge";
		String memberPictureUrl = badge.getAccount().getProfilePictureUrl();
		RecentActivity activity = new RecentActivity(memberPictureUrl, message, badge.getImageUrl());
		return activity;
	}
}
