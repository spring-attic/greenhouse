package com.springsource.greenhouse.invite;

import com.springsource.greenhouse.connect.AccountReference;
import com.springsource.greenhouse.signup.SignupForm;

public final class InviteDetails {
	
	private final Invitee invitee;
	
	private final AccountReference sentBy;

	public InviteDetails(Invitee invitee, AccountReference sentBy) {
		this.invitee = invitee;
		this.sentBy = sentBy;
	}

	public Invitee getInvitee() {
		return invitee;
	}

	public AccountReference getSentBy() {
		return sentBy;
	}

	public SignupForm createSignupForm() {
		SignupForm form = new SignupForm();
		form.setFirstName(invitee.getFirstName());
		form.setLastName(invitee.getLastName());
		form.setEmail(invitee.getEmail());
		return form;
	}
	
}
