package com.springsource.greenhouse.signin;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class GreenhouseUserDetailsRowMapper implements RowMapper<GreenhouseUserDetails> {

	public GreenhouseUserDetails mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		return new GreenhouseUserDetails(rs.getLong("id"), rs.getString("username"), rs.getString("password"), rs.getString("firstName"));
	}
	
}