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
 * A SimpleAction definition.
 * Has a single String 'type' field that captures the type of action that was performed e.g signedUp.
 * @author Keith Donald
 */
public final class SimpleAction extends Action {
	
	private final String type;

	public SimpleAction(String type, Long id, DateTime time, Account account, Location location) {
		super(id, time, account, location);
		this.type = type;
	}

	/**
	 * The type of action that was performed.
	 */
	public String getType() {
		return type;
	}
	
	public String toString() {
		// TODO do not hardcode
		String actionText = "SignedUp".equals(getType()) ? "signed up" : getType();
		return getAccount().getFullName() + " " + actionText;
	}

}