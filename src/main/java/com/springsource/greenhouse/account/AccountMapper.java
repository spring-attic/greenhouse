package com.springsource.greenhouse.account;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.util.UriTemplate;

public class AccountMapper implements RowMapper<Account> {
	
	private final PictureUrlMapper pictureUrlMapper;
	
	private final UriTemplate profileUrlTemplate;
	
	public AccountMapper(PictureUrlMapper pictureUrlMapper, UriTemplate profileUrlTemplate) {
		this.pictureUrlMapper = pictureUrlMapper;
		this.profileUrlTemplate = profileUrlTemplate;
	}

	public Account mapRow(ResultSet rs, int row) throws SQLException {
		return new Account(rs.getLong("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), 
				pictureUrlMapper.mapRow(rs, row), profileUrlTemplate);
	}
	
}