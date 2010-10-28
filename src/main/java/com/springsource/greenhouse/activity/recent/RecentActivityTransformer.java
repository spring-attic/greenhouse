/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.activity.recent;

import org.springframework.integration.annotation.Transformer;

import com.springsource.greenhouse.activity.action.Action;
import com.springsource.greenhouse.activity.badge.AwardedBadge;

/**
 * Transforms Actions and AwardedBadges to RecentActivity objects.
 * @author Keith Donald
 */
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
