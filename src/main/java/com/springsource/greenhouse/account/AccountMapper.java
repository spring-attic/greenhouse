package com.springsource.greenhouse.account;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


class AccountMapper implements RowMapper<Account> {
	
	private ProfilePictureUrlMapper profilePictureUrlMapper = new ProfilePictureUrlMapper(PictureSize.small);
	
	public Account mapRow(ResultSet rs, int row) throws SQLException {
		return new Account(rs.getLong("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), 
				profilePictureUrlMapper.mapRow(rs, row));
	}
	
}