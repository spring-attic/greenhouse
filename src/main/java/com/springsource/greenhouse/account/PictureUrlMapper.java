/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.account;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * A RowMapper that maps a group of Account fields to a absolute profile picture URL String using a {@link PictureUrlFactory} and {@link PictureSize}.
 * @author Keith Donald
 */
public final class PictureUrlMapper implements RowMapper<String> {

	private final PictureUrlFactory urlFactory;
	
	private final PictureSize pictureSize;
	
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