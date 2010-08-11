package com.springsource.greenhouse.action;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("actionRepository")
public class JdbcActionRepository implements ActionRepository {

	private JdbcTemplate jdbcTemplate;
	
	@Inject
	public JdbcActionRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public SimpleAction createSimpleAction(String type, Long accountId, Location location) {
		DateTime performTime = new DateTime(DateTimeZone.UTC);
		Float latitude = location != null ? location.getLatitude() : null;
		Float longitude = location != null ? location.getLongitude() : null;
		jdbcTemplate.update("insert into MemberAction (actionType, performTime, latitude, longitude, member) values (?, ?, ?, ?)", type, performTime, latitude, longitude, accountId);
		Long id = jdbcTemplate.queryForLong("call identity()");
		return new SimpleAction(type, id, performTime, accountId, location);
	}

}
