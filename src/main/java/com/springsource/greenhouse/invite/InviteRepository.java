package com.springsource.greenhouse.invite;

import com.springsource.greenhouse.account.Account;

public interface InviteRepository {

	boolean alreadyInvited(String email);
	
	void saveInvite(String token, Invitee invitee, String text, Long sentBy);
	
	void markInviteAccepted(String token, Account account);

	Invite findInvite(String token) throws NoSuchInviteException, InviteAlreadyAcceptedException;

}