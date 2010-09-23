package com.springsource.greenhouse.account;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PictureUrlMapper implements RowMapper<String> {

	private PictureUrlFactory urlFactory;
	
	private PictureSize pictureSize;
	
	public PictureUrlMapper(PictureUrlFactory urlFactory, PictureSize pictureSize) {
		this.urlFactory = urlFactory;
		this.pictureSize = pictureSize;
	}

	public String mapRow(ResultSet rs, int row) throws SQLException {
		Gender gender = Gender.valueOf(rs.getString("gender").charAt(0));
		return urlFactory.pictureUrl(rs.getLong("id"), pictureSize, rs.getBoolean("pictureSet"), gender);
	}

	public String defaultPictureUrl(Gender gender) {
		return urlFactory.defaultPictureUrl(gender, pictureSize);
	}
}