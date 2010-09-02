package com.springsource.greenhouse.account;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ProfilePictureUrlMapper implements RowMapper<String> {

	private PictureSize pictureSize;
	
	public ProfilePictureUrlMapper(PictureSize pictureSize) {
		this.pictureSize = pictureSize;
	}

	public String mapRow(ResultSet rs, int row) throws SQLException {
		return ProfileUrlUtils.pictureUrl(rs.getLong("id"), pictureSize, rs.getBoolean("pictureSet"), Gender.valueOf(rs.getString("gender").charAt(0)));
	}
}