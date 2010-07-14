package com.springsource.greenhouse.members;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class DefaultMembersService implements MembersService {

	private final JdbcTemplate jdbcTemplate;

	@Inject
	public DefaultMembersService(JdbcTemplate jdbcTemplate) {
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
}
