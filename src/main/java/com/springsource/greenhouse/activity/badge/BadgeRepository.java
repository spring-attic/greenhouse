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

import com.springsource.greenhouse.account.Account;

import com.springsource.greenhouse.activity.action.Action;

/**
 * Manages records of awarded badges.
 * @author Keith Donald
 */
public interface BadgeRepository {
	
	/**
	 * Save a record of an Awarded badge in this repository.
	 * @param badge the name of the badge
	 * @param account the member account being awarded the badge
	 * @param action the action that is cause for the award
	 */
	AwardedBadge createAwardedBadge(String badge, Account account, Action action);
	
}
