package com.springsource.greenhouse.badge;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class JdbcBadgeRepository implements BadgeRepository {

	private JdbcTemplate jdbcTemplate;
	
	@Inject
	public JdbcBadgeRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Transactional
	public AwardedBadge createAwardedBadge(String badge, Long accountId, Long actionId) {
		DateTime awardTime = new DateTime(DateTimeZone.UTC);
		jdbcTemplate.update("insert into AwardedBadge (badge, awardTime, member, memberAction) values (?, ?, ?, ?)", badge, awardTime.toDate(), accountId, actionId);
		Long id = jdbcTemplate.queryForLong("call identity()");
		return new AwardedBadge(id, badge, awardTime, accountId, actionId);
	}

}