package com.springsource.greenhouse.badge;

import org.springframework.integration.annotation.Transformer;

import com.springsource.greenhouse.recent.RecentActivity;

public class AwardedBadgeRecentActivityTransformer {

	// TODO text should be localized
	@Transformer
	public RecentActivity forAwardedBadge(AwardedBadge badge) {
		String memberPictureUrl = badge.getAccount().getProfilePictureUrl();
		if (memberPictureUrl == null) {
			// TODO vary url based on gender
			// TODO don't hardcode hostname
			memberPictureUrl = "http://localhost:8080/resources/members/male.jpg";
		}
		String text = badge.getAccount().getFullName() + " was awarded the " + badge.getName() + " badge";
		RecentActivity activity = new RecentActivity(memberPictureUrl, text, badge.getImageUrl());
		return activity;
	}
}
