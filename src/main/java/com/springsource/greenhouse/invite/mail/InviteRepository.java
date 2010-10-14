package com.springsource.greenhouse.invite.mail;

public interface InviteRepository {

	void saveInvite(String token, Invitee sentTo, String text, Long sentBy);

	void removeInvite(String token);

}