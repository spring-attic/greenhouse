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
package com.springsource.greenhouse.groups;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * GroupRepository implementation that stores Group information in a relational database using the JDBC API.
 * @author Keith Donald
 */
@Repository
public class JdbcGroupRepository implements GroupRepository {
	
	private final JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcGroupRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;}
	
	public Group findGroupBySlug(String profileKey) {
		return jdbcTemplate.queryForObject(FIND_GROUP_QUERY, groupMapper, profileKey);
	}
	
	// internal helpers
	
	private RowMapper<Group> groupMapper = new RowMapper<Group>() {
		public Group mapRow(ResultSet rs, int row) throws SQLException {
			Group group = new Group();
			group.setName(rs.getString("name"));
			group.setDescription(rs.getString("description"));
			group.setHashtag(rs.getString("hashtag"));
			return group;
		}
	};
	
	private static final String FIND_GROUP_QUERY = "select name, description, hashtag from MemberGroup where slug = ?";
}
