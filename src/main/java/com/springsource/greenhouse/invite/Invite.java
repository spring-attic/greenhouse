package com.springsource.greenhouse.invite;

import com.springsource.greenhouse.connect.AccountReference;
import com.springsource.greenhouse.signup.SignupForm;

public final class Invite {
	
	private final Invitee invitee;
	
	private final AccountReference sentBy;

	private final boolean accepted;
	
	public Invite(Invitee invitee, AccountReference sentBy, boolean accepted) {
		this.invitee = invitee;
		this.sentBy = sentBy;
		this.accepted = accepted;
	}

	public Invitee getInvitee() {
		return invitee;
	}

	public AccountReference getSentBy() {
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
