package com.springsource.greenhouse.groups;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcGroupRepository implements GroupRepository {
	
	private final JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcGroupRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;}
	
	public Group findGroupBySlug(String profileKey) {
		return jdbcTemplate.queryForObject(FIND_GROUP_QUERY, groupMapper, profileKey);
	}
	
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
