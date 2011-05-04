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

import com.springsource.greenhouse.account.Account;

/**
 * Records Action that are performed in the system of record.
 * @author Keith Donald
 */
public interface ActionRepository {

	/**
	 * Record that a member Account performed a SimpleAction of the specified type.
	 * The timestamp of the Action is set to now().
	 * The Location the Action was performed is expected to be resolved against a well-known Thread context variable.
	 * @param type the simple action type e.g. signedUp.
	 * @param account the member that performed the SimpleAction
	 * @return the SimpleAction
	 */
	SimpleAction saveSimpleAction(String type, Account account);

	/**
	 * Record that a member Account performed an Action.
	 * @param <A> the Action type, a subclass of {@link Action}.
	 * @param actionClass the Action subclass
	 * @param account the member that performed the Action
	 * @param actionFactory a factory for constructing the specific Action specialization.  Provides a callback that allows the caller to populate the Action instance.
	 * @return the Action of type A.
	 */
	<A extends Action> A saveAction(Class<A> actionClass, Account account, ActionFactory<A> actionFactory);
}