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
package com.springsource.greenhouse.activity.badge;

import com.springsource.greenhouse.activity.action.Action;

/**
 * Simple Badge Award rule that always awards a badge when invoked.
 * @author Keith Donald
 */
public final class SimpleBadgeAwarder implements BadgeAwarder<Action> {

	private final String badgeName;
	
	private final BadgeRepository badgeRepository;
	
	/**
	 * Creates a SimpleBadgeAwarder.
	 * @param badgeName the name of the badge to award
	 * @param badgeRepository the badge repository used to save the record of the awarded badge
	 */
	public SimpleBadgeAwarder(String badgeName, BadgeRepository badgeRepository) {
		this.badgeName = badgeName;
		this.badgeRepository = badgeRepository;
	}

	public AwardedBadge awardBadgeForAction(Action action) {
		return badgeRepository.createAwardedBadge(badgeName, action.getAccount(), action);
	}

}
