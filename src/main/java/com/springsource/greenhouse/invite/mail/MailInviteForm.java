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

import org.hibernate.validator.constraints.NotEmpty;

import com.springsource.greenhouse.invite.Invitee;

/**
 * Model backing the email invite form.
 * @author Keith Donald
 */
public class MailInviteForm {

	@NotEmpty
	private List<Invitee> invitees;
	
	@NotEmpty
	private String invitationText;

	/**
	 * The list of inviteees to send the invite to.
	 * Submitted as a comma-delimited string value and converted to a List by the conversion system.
	 */
	public List<Invitee> getInvitees() {
		return invitees;
	}

	public void setInvitees(List<Invitee> invitees) {
		this.invitees = invitees;
	}

	/**
	 * The body text of the invitation.
	 * May be personalized by the member sending out the invite.
	 */
	public String getInvitationText() {
		return invitationText;
	}

	public void setInvitationText(String invitationText) {
		this.invitationText = invitationText;
	}
	
}
