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
package com.springsource.greenhouse.invite;

import org.joda.time.DateTime;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.activity.action.Action;
import com.springsource.greenhouse.utils.Location;

/**
 * Indicates that a invitee has accepted his or her invite by signing up as a member of our community.
 * @author Keith Donald
 */
public final class InviteAcceptAction extends Action {

	private final Long sentBy;
	
	private final DateTime sentTime;
	
	public InviteAcceptAction(Long id, DateTime time, Account account, Location location, Long sentBy, DateTime sentTime) {
		super(id, time, account, location);
		this.sentBy = sentBy;
		this.sentTime = sentTime;
	}
	
	/**
	 * The id of the member account that sent the invite.
	 */
	public Long getSentBy() {
		return sentBy;
	}

	/**
	 * The time the invite was originally sent.
	 */
	public DateTime getSentTime() {
		return sentTime;
	}
	
	public String toString() {
		// TODO Account.gender needs to be preserved for his/her text
		return getAccount().getFullName() + " accepted his Greenhouse invitation";
	}
	
}
