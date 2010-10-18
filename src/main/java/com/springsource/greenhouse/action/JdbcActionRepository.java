package com.springsource.greenhouse.action;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.utils.Location;

public class JdbcActionRepository implements ActionRepository {

	private final JdbcTemplate jdbcTemplate;
	
	private final ActionGateway actionGateway;
	
	@Inject
	public JdbcActionRepository(JdbcTemplate jdbcTemplate, ActionGateway actionGateway) {
		this.jdbcTemplate = jdbcTemplate;
		this.actionGateway = actionGateway;
	}

	@Transactional
	public SimpleAction createSimpleAction(String type, Account account) {
		Location location =	Location.getCurrentLocation();
		DateTime performTime = new DateTime(DateTimeZone.UTC);
		Long id = saveAction(type, performTime, account, location);
		SimpleAction action = new SimpleAction(type, id, performTime, account, location);
		actionGateway.actionPerformed(action);
		return action;
	}

	@Transactional
	public <A extends Action> A createAction(Class<A> actionClass, Account account, ActionFactory<A> actionFactory) {
		Location location =	Location.getCurrentLocation();
		DateTime performTime = new DateTime(DateTimeZone.UTC);
		Long id = saveAction(actionType(actionClass), performTime, account, location);
		A action = actionFactory.createAction(id, performTime, account, location);
		actionGateway.actionPerformed(action);
		return action;
	}

	// internal helpers

	private String actionType(Class<? extends Action> actionClass) {
		String shortName = ClassUtils.getShortName(actionClass);
		int actionPart = shortName.lastIndexOf("Action");
		return actionPart == -1 ? shortName : shortName.substring(0, actionPart);
	}
	
	private Long saveAction(String type, DateTime performTime, Account account, Location location) {
		Double latitude = location != null ? location.getLatitude() : null;
		Double longitude = location != null ? location.getLongitude() : null;
		jdbcTemplate.update("insert into MemberAction (actionType, performTime, latitude, longitude, member) values (?, ?, ?, ?, ?)", type, performTime.toDate(), latitude, longitude, account.getId());
		return jdbcTemplate.queryForLong("call identity()");
	}

}