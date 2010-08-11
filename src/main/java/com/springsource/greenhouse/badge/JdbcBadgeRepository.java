package com.springsource.greenhouse.badge;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcBadgeRepository implements BadgeRepository {

	private JdbcTemplate jdbcTemplate;
	
	@Inject
	public JdbcBadgeRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public AwardedBadge createAwardedBadge(String badgeName, Long accountId, Long actionId) {
		DateTime awardTime = new DateTime(DateTimeZone.UTC);
		jdbcTemplate.update("insert into AwardedBadge (badgeName, awardTime, member, memberAction) values (?, ?, ?, ?)", badgeName, awardTime, accountId, actionId);
		Long id = jdbcTemplate.queryForLong("call identity()");
		return new AwardedBadge(id, badgeName, awardTime, accountId, actionId);
	}

}
