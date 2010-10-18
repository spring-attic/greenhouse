package com.springsource.greenhouse.invite;

import com.springsource.greenhouse.account.Account;

public interface InviteRepository {

	void saveInvite(String token, Invitee invitee, String text, Long sentBy);

	void markInviteAccepted(String token, Account signedUp);

	Invite getInvite(String token) throws NoSuchInviteException, InviteAlreadyAcceptedException;

}