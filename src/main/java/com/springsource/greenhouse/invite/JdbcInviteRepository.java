package com.springsource.greenhouse.invite;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.springsource.greenhouse.connect.AccountReference;

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
		return jdbcTemplate.queryForObject(SELECT_INVITE_DETAILS, new RowMapper<InviteDetails>() {
			public InviteDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
				Invitee invitee = new Invitee(rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"));
				AccountReference sentBy = AccountReference.textOnly(rs.getLong("sentById"), rs.getString("sentByUsername"), rs.getString("sentByFirstName"), rs.getString("sentByLastName"));
				return new InviteDetails(invitee, sentBy);
			}
		}, token);
	}

	private static final String INSERT_INVITE = "insert into Invite (token, email, firstName, lastName, text, sentBy) values (?, ?, ?, ?, ?, ?)";

	private static final String SELECT_INVITE_DETAILS = "select i.email, i.firstName, i.lastName, m.id as sentById, m.username as sentByUsername, m.firstName as sentByFirstName, m.lastName as sentByLastName from Invite i inner join Member m on i.sentBy = m.id where i.token = ?";

}
