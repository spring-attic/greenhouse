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

import org.joda.time.DateTime;
import com.springsource.greenhouse.account.Account;

import com.springsource.greenhouse.activity.action.Action;

/**
 * The details about a badge that was awarded to a member.
 * @author Keith Donald
 */
public final class AwardedBadge {
	
	private final Long id;
	
	private final String name;
		
	private final DateTime awardTime;

	private final String imageUrl;
	
	private final Account account;
	
	private final Action action;
	
	public AwardedBadge(Long id, String name, DateTime awardTime, String imageUrl, Account account, Action action) {
		this.id = id;
		this.name = name;
		this.awardTime = awardTime;
		this.imageUrl = imageUrl;
		this.account = account;
		this.action = action;
	}

	/**
	 * The internal id of the badge award.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * The name of the badge that was awarded.
	 */
	public String getName() {
		return name;
	}

	/**
	 * The time the award was presented.
	 */
	public DateTime getAwardTime() {
		return awardTime;
	}

	/**
	 * A link to a picture of the award.
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * The member account that was awarded the badge.
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * The action that is the cause for the award.
	 */
	public Action getAction() {
		return action;
	}
	
}