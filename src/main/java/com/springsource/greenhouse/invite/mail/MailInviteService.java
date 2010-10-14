package com.springsource.greenhouse.invite.mail;

import java.util.List;

import com.springsource.greenhouse.account.Account;

public interface MailInviteService {

	void sendInvite(Account account, List<Invitee> to, String invitationText);

	void acceptInvite(String token);

}
