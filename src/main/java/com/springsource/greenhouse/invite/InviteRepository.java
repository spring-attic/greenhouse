package com.springsource.greenhouse.invite;

public interface InviteRepository {

	void saveInvite(String token, Invitee invitee, String text, Long sentBy);

	void removeInvite(String token);

	InviteDetails getInviteDetails(String token);

}