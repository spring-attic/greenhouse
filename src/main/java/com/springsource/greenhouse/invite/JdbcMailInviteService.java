package com.springsource.greenhouse.invite;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class JdbcMailInviteService implements MailInviteService {

	public void sendInvite(String invite, List<Invitee> to) {
		
	}

}
