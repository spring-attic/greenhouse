package com.springsource.greenhouse.invite;

import com.springsource.greenhouse.account.ProfileReference;
import com.springsource.greenhouse.signup.SignupForm;

public final class Invite {
	
	private final Invitee invitee;
	
	private final ProfileReference sentBy;

	private final boolean accepted;
	
	public Invite(Invitee invitee, ProfileReference sentBy, boolean accepted) {
		this.invitee = invitee;
		this.sentBy = sentBy;
		this.accepted = accepted;
	}

	public Invitee getInvitee() {
		return invitee;
	}

	public ProfileReference getSentBy() {
		return sentBy;
	}

	public boolean isAccepted() {
		return accepted;
	}
	
	public SignupForm createSignupForm() {
		SignupForm form = new SignupForm();
		form.setFirstName(invitee.getFirstName());
		form.setLastName(invitee.getLastName());
		form.setEmail(invitee.getEmail());
		form.setConfirmEmail(invitee.getEmail());
		return form;
	}

}
