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
package com.springsource.greenhouse.events;

import javax.inject.Inject;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.activity.badge.AwardedBadge;
import com.springsource.greenhouse.activity.badge.BadgeAwarder;
import com.springsource.greenhouse.activity.badge.BadgeRepository;

/**
 * Awards the "Broadcaster" badge to a member the first time they tweet about an Event from one of its Venues.
 * TODO: this class is not yet used.
 * @author Keith Donald
 * @see EventTweetAction
 */
public class BroadcasterBadgeAwarder implements BadgeAwarder<EventTweetAction> {

	private final BadgeRepository badgeRepository;
	
	@Inject
	public BroadcasterBadgeAwarder(BadgeRepository badgeRepository) {
		this.badgeRepository = badgeRepository;
	}
	
	public AwardedBadge awardBadgeForAction(EventTweetAction action) {
		if (alreadyAwarded(action.getAccount(), action.getEventId())) {
			return null;
		}
		return badgeRepository.createAwardedBadge(BADGE_NAME, action.getAccount(), action);
	}

	// internal helpers
	
	private boolean alreadyAwarded(Account account, Long eventId) {
		return false;
	}
	
	private static final String BADGE_NAME = "Broadcaster";

}