package com.springsource.greenhouse.invite;

import java.util.List;

import javax.validation.constraints.NotNull;

public class MailInviteForm {

	@NotNull
	private List<Invitee> invitees;
	
	@NotNull
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
