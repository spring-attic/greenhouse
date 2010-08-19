package com.springsource.greenhouse.action;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.springsource.greenhouse.UserLocationHandlerInterceptor;
import com.springsource.greenhouse.account.Account;

public class JdbcActionRepository implements ActionRepository {

	private JdbcTemplate jdbcTemplate;
	
	@Inject
	public JdbcActionRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Transactional
	public SimpleAction createSimpleAction(String type, Account account) {
		Location location = resolveUserLocation(account.getId());
		DateTime performTime = new DateTime(DateTimeZone.UTC);
		Float latitude = location != null ? location.getLatitude() : null;
		Float longitude = location != null ? location.getLongitude() : null;
		jdbcTemplate.update("insert into MemberAction (actionType, performTime, latitude, longitude, member) values (?, ?, ?, ?, ?)", type, performTime.toDate(), latitude, longitude, account.getId());
		Long id = jdbcTemplate.queryForLong("call identity()");
		return new SimpleAction(type, id, performTime, account, location);
	}

	// TODO this web-specific dependency is probably less than ideal
	private Location resolveUserLocation(Long accountId) {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (attributes != null) {
			return (Location) attributes.getAttribute(UserLocationHandlerInterceptor.USER_LOCATION_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		} else {
			return null;
		}
	}

}
