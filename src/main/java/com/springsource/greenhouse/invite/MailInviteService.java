package com.springsource.greenhouse.invite;

import java.util.List;

public interface MailInviteService {

	void sendInvite(String invite, List<Invitee> to);

}
