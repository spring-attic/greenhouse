package com.springsource.greenhouse.invite;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcInviteRepository implements InviteRepository {

	private final JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcInviteRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void saveInvite(String token, Invitee invitee, String text, Long sentBy) {
		jdbcTemplate.update(INSERT_INVITE, token, invitee.getEmail(), invitee.getFirstName(), invitee.getLastName(), text, sentBy);
	}
		
	public void removeInvite(String token) {
		jdbcTemplate.update("delete from Invite where token = ?", token);
	}
	
	public InviteDetails getInviteDetails(String token) {
		return null;
	}

	private static final String INSERT_INVITE = "insert into Invite (token, email, firstName, lastName, text, sentBy) values (?, ?, ?, ?, ?, ?)";

}
