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
 * Callback interface for constructing an Action instance.
 * @author Keith Donald
 * @param <A> the Action type
 */
public interface ActionFactory<A extends Action> {
	
	/**
	 * Create an Action instance of type A.
	 * This callback allows the caller to specify the Action implementation and populate its specific fields.
	 * It also allows the additional properties of this Action specialization to be persisted with the system record, if required.
	 * @param id the assigned internal action id
	 * @param time the time the action was performed, just now
	 * @param account the member who performed the action
	 * @param location the resolved member location where the action was performed
	 * @return the Action instance
	 */
	A createAction(Long id, DateTime time, Account account, Location location);
}