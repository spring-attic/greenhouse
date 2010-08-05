package com.springsource.greenhouse.members;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class JdbcMemberRepository implements MemberRepository {

	private final JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public Member findMemberByProfileKey(String profileKey) {
		Long accountId = getAccountId(profileKey);
		return accountId != null ? findMemberByAccountId(accountId) : findMemberByUsername(profileKey);
	}
	
	public Member findMemberByAccountId(Long accountId) {
		return jdbcTemplate.queryForObject("select firstName, lastName from Member where id = ?", memberMapper, accountId);
	}

	public Member findMemberByUsername(String username) {
		return jdbcTemplate.queryForObject("select firstName, lastName from Member where username = ?", memberMapper, username);
	}
	
	
	public Map<String, String> lookupConnectedAccountIds(String profileKey) {
		final Map<String, String> connectedIds = new HashMap<String, String>();

		Object key = profileKey;
		String query = BASE_CONNECTED_ID_QUERY + USERNAME_CLAUSE; 
		try {
			key = Long.parseLong(profileKey);
			query = BASE_CONNECTED_ID_QUERY + ID_CLAUSE;
		} catch (NumberFormatException e) {}
		
		jdbcTemplate.query(query,
				new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				connectedIds.put(rs.getString("provider"), rs.getString("accountId"));
			}
		}, key);
		
		return connectedIds;
	}	

	private RowMapper<Member> memberMapper = new RowMapper<Member>() {
		public Member mapRow(ResultSet rs, int row) throws SQLException {
			Member member = new Member();
			member.setFirstName(rs.getString("firstName"));
			member.setLastName(rs.getString("lastName"));
			return member;
		}
	};

	private Long getAccountId(String id) {
		try {
			return Long.parseLong(id);
		} catch (NumberFormatException e) {
			return null;
		}		
	}
	
	private static final String BASE_CONNECTED_ID_QUERY = "select provider, accountid from connectedaccount, member where member = id ";
	private static final String USERNAME_CLAUSE = "and username = ?";
	private static final String ID_CLAUSE = "and id = ?";
}
