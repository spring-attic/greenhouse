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
package com.springsource.greenhouse.activity.action;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.utils.Location;

/**
 * An ActionRepository that records Action information in a relational database using the JDBC API.
 * @author Keith Donald
 */
@Repository
public class JdbcActionRepository implements ActionRepository {

	private final JdbcTemplate jdbcTemplate;
	
	private final ActionGateway actionGateway;
	
	@Inject
	public JdbcActionRepository(JdbcTemplate jdbcTemplate, ActionGateway actionGateway) {
		this.jdbcTemplate = jdbcTemplate;
		this.actionGateway = actionGateway;
	}

	public SimpleAction saveSimpleAction(final String type, Account account) {
		SimpleAction action = doSaveAction(SimpleAction.class, account, new ActionFactory<SimpleAction>() {
			public SimpleAction createAction(Long id, DateTime time, Account account, Location location) {
				return new SimpleAction(type, id, time, account, location);
			}			
		}, type, Location.getCurrentLocation(), new DateTime(DateTimeZone.UTC));
		actionGateway.actionPerformed(action);
		return action;
	}

	public <A extends Action> A saveAction(Class<A> actionClass, Account account, ActionFactory<A> actionFactory) {
		A action = doSaveAction(actionClass, account, actionFactory, actionType(actionClass), Location.getCurrentLocation(), new DateTime(DateTimeZone.UTC));
		actionGateway.actionPerformed(action);
		return action;
	}

	// internal helpers

	@Transactional
	private <A extends Action> A doSaveAction(Class<A> actionClass, Account account, ActionFactory<A> actionFactory, String actionType, Location location, DateTime time) {
		Long id = insertAction(actionType, time, account, location);
		return actionFactory.createAction(id, time, account, location);
	}

	private Long insertAction(String type, DateTime performTime, Account account, Location location) {
		Double latitude = location != null ? location.getLatitude() : null;
		Double longitude = location != null ? location.getLongitude() : null;
		jdbcTemplate.update("insert into MemberAction (actionType, performTime, latitude, longitude, member) values (?, ?, ?, ?, ?)", type, performTime.toDate(), latitude, longitude, account.getId());
		return jdbcTemplate.queryForLong("call identity()");
	}
	
	private String actionType(Class<? extends Action> actionClass) {
		String shortName = ClassUtils.getShortName(actionClass);
		int actionPart = shortName.lastIndexOf("Action");
		return actionPart == -1 ? shortName : shortName.substring(0, actionPart);
	}

}