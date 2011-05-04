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
package com.springsource.greenhouse.members;

import java.util.List;

/**
 * Data access interface for public member profiles.
 * @author Keith Donald
 */
public interface ProfileRepository {

	/**
	 * Find a profile by it is id.
	 * The id may be the member's public username or their internal account id if no username is set.
	 */
	Profile findById(String profileId);

	/**
	 * Find a profile by the member's internal account identifier.
	 */
	Profile findByAccountId(Long accountId);

	/**
	 * Get references to the member's profiles at service providers the member has connected their local account with.
	 * For example, the member may have connected their local account with their Facebook account.
	 * In that case, a ConnectedProfile instance pointing to their Facebook profile will be returned by this method. 
	 */
	List<ConnectedProfile> findConnectedProfiles(Long accountId);

}