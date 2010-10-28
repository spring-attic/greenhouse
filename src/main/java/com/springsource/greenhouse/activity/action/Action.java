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
package com.springsource.greenhouse.activity.action;

import org.joda.time.DateTime;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.utils.Location;

/**
 * Models an action a member performed in the application system.
 * Used to record member activity.
 * Actions also serve as input into the badge system (performing an Action may result in a badge being awarded).
 * Actions may be explicitly performed e.g. "Keith tweeted from SpringOne2gx", or implicitly triggered on behalf of a member
 * e.g. "10 minutes before the SpringOne event ended, the event organizer awarded the top speaker badges (triggered by a timer)."
 * @author Keith Donald
 */
public abstract class Action {
	
	private final Long id;

	private final DateTime time;
	
	private final Account account;
	
	private final Location location;
	
	public Action(Long id, DateTime time, Account account, Location location) {
		this.id = id;
		this.time = time;
		this.account = account;
		this.location = location;
	}

	/**
	 * The internal identifier of this Action.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * The date and time the Action was performed.
	 */
	public DateTime getTime() {
		return time;
	}

	/**
	 * The member Account that performed the Action.
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * The Location where the Action was performed.
	 * May be null if geo-tagging was disabled.
	 */
	public Location getLocation() {
		return location;
	}
	
}