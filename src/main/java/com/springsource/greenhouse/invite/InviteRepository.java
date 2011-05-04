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

import com.springsource.greenhouse.account.Account;

/**
 * Data access interface for invites.
 * @author Keith Donald
 */
public interface InviteRepository {

	/**
	 * True if an invite has already been sent to the email address.
	 * Used to skip sending an invite if one is already outstanding.
	 */
	boolean alreadyInvited(String email);
	
	/**
	 * Saves an invite to the repository.
	 * Called after sending the invite out to the invitee for the first time.
	 * @param token the token that uniquely identifies the invite and can be used to look it up later (see {@link #findInvite(String)})
	 * @param invitee the invitee the invite was sent to
	 * @param text the invitation text for the invitee
	 * @param sentBy the id of the member account that sent the invite
	 */
	void saveInvite(String token, Invitee invitee, String text, Long sentBy);
	
	/**
	 * Update the invite to indicate it has been accepted by the invitee.
	 * Called after the invite has been accepted.
	 * @param token the token that uniquely identifies the invite
	 * @param account the new member account that was created for the invitee when they accepted the invite
	 */
	void markInviteAccepted(String token, Account account);

	/**
	 * Lookup a Invite record by its assigned token.
	 * Used to retrieve the Invite on behalf of the Invitee so they can review it and accept it.
	 * @param token the invite token
	 * @return the invite
	 * @throws NoSuchInviteException no such invite could be found with that token
	 * @throws InviteAlreadyAcceptedException the invite has already been accepted, so there's no reason to look it up anymore
	 */
	Invite findInvite(String token) throws NoSuchInviteException, InviteAlreadyAcceptedException;

}