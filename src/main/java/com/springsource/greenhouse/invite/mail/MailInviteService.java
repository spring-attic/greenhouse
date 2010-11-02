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
package com.springsource.greenhouse.invite.mail;

import java.util.List;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.invite.Invitee;

/**
 * A service for sending invites out by email.
 * @author Keith Donald
 */
public interface MailInviteService {

	/**
	 * Send an invite from the member to the list of invitees.
	 * @param from the member account
	 * @param to the list of invitees
	 * @param invitationBody the member's personalized invitation body, which may be a template that contains $invitee$ variable references referring to Invitee properties.
	 */
	void sendInvite(Account from, List<Invitee> to, String invitationBody);

}
