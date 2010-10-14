package com.springsource.greenhouse.invite;

public interface InviteRepository {

	void saveInvite(String token, String email, String firstName, String lastName, String text, Long sentBy);

	void removeInvite(String token);

}