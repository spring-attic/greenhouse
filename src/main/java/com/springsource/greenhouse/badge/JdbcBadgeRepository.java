package com.springsource.greenhouse.badge;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.jdbc.core.JdbcTemplate;
import com.springsource.greenhouse.account.Account;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.action.Action;

public class JdbcBadgeRepository implements BadgeRepository {

	private JdbcTemplate jdbcTemplate;
	
	@Inject
	public JdbcBadgeRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Transactional
	public AwardedBadge createAwardedBadge(String badge, Account account, Action action) {
		DateTime awardTime = new DateTime(DateTimeZone.UTC);
		jdbcTemplate.update("insert into AwardedBadge (badge, awardTime, member, memberAction) values (?, ?, ?, ?)", badge, awardTime.toDate(), account.getId(), action.getId());
		Long id = jdbcTemplate.queryForLong("call identity()");
		String imageUrl = "http://images.greenhouse.springsource.org/badges/" + badge + ".jpg";
		return new AwardedBadge(id, badge, awardTime, imageUrl, account, action);
	}

}