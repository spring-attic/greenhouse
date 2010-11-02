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

import com.springsource.greenhouse.account.ProfileReference;
import com.springsource.greenhouse.signup.SignupForm;

/**
 * A invitation to join our community sent to an invitee by a member.
 * @author Keith Donald
 */
public final class Invite {
	
	private final Invitee invitee;
	
	private final ProfileReference sentBy;

	private final boolean accepted;
	
	public Invite(Invitee invitee, ProfileReference sentBy, boolean accepted) {
		this.invitee = invitee;
		this.sentBy = sentBy;
		this.accepted = accepted;
	}

	/**
	 * The person this invite is addressed to.
	 */
	public Invitee getInvitee() {
		return invitee;
	}

	/**
	 * A reference to the profile of the member who sent the invite.
	 * Used to link to the member's profile when displaying the invite to the invitee.
	 */
	public ProfileReference getSentBy() {
		return sentBy;
	}

	/**
	 * Whether or not this invite has been accepted.
	 */
	public boolean isAccepted() {
		return accepted;
	}

	/**
	 * A factory method that populates a {@link SignupForm} model from this Invite.
	 * Pre-populates fields to make it as simple as possible for the invitee to sign up.
	 */
	public SignupForm createSignupForm() {
		SignupForm form = new SignupForm();
		form.setFirstName(invitee.getFirstName());
		form.setLastName(invitee.getLastName());
		form.setEmail(invitee.getEmail());
		form.setConfirmEmail(invitee.getEmail());
		return form;
	}

}