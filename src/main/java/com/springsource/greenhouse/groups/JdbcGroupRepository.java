package com.springsource.greenhouse.groups;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcGroupRepository implements GroupRepository {

	private JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcGroupRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public GroupEvent findEventByMonthOfYear(String group, Integer month, Integer year) {
		// TODO
		return null;
	}

	
}