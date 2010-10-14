package com.springsource.greenhouse.invite.mail;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class MailInviteForm {

	@NotEmpty
	private List<Invitee> invitees;
	
	@NotEmpty
	private String invitationText;

	public List<Invitee> getInvitees() {
		return invitees;
	}

	public void setInvitees(List<Invitee> invitees) {
		this.invitees = invitees;
	}

	public String getInvitationText() {
		return invitationText;
	}

	public void setInvitationText(String invitationText) {
		this.invitationText = invitationText;
	}
	
}
