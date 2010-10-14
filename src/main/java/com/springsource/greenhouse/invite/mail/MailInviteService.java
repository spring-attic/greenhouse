package com.springsource.greenhouse.invite.mail;

import java.util.List;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.invite.Invitee;

public interface MailInviteService {

	void sendInvite(Account account, List<Invitee> to, String invitationText);

	void acceptInvite(String token);

}
