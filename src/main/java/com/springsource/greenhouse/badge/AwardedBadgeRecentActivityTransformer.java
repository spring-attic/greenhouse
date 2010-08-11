package com.springsource.greenhouse.badge;

import org.springframework.integration.annotation.Transformer;

import com.springsource.greenhouse.recent.RecentActivity;

public class AwardedBadgeRecentActivityTransformer {

	@Transformer
	public RecentActivity forAwardedBadge(AwardedBadge badge) {
		String message = "Keith Donald was awarded the " + badge.getName() + " badge";
		String memberPictureUrl = "http://localhost:8080/greenhouse/resources/recent/genericMember.png";
		String imageUrl = "http://localhost:8080/greenhouse/resources/recent/genericAction.png";
		RecentActivity activity = new RecentActivity(memberPictureUrl, message, imageUrl);
		return activity;
	}
}
