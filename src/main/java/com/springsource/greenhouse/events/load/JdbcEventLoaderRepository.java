/*
 /*
 * Copyright 2012 the original author or authors.
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
package com.springsource.greenhouse.events.load;

import static java.sql.Types.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * EventLoaderRepository implementation that loads Event data into a relational database using the JDBC API.
 * @author CraigWalls
 */
@Repository
public class JdbcEventLoaderRepository implements EventLoaderRepository {

	private static final Logger logger = LoggerFactory.getLogger(JdbcEventLoaderRepository.class);

	private final JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcEventLoaderRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Transactional
	public long loadEvent(EventData eventData, VenueData venueData) {
		try {
			// update if it already exists
			int eventId = jdbcTemplate.queryForInt(SELECT_EXTERNAL_EVENT, eventData.getSourceId(), eventData.getSource()); // TODO: Fetch date for comparison check
			jdbcTemplate.update(UPDATE_EVENT, eventData.getName(), eventData.getDescription(), eventData.getTimeZone(), eventData.getFirstDay(), eventData.getLastDay(), eventData.getAbbreviation(), eventId);
			logger.info("Updated event (ID = " + eventId + ")");
			int venueId = jdbcTemplate.queryForInt(SELECT_EVENT_VENUE, eventId);
			jdbcTemplate.update(UPDATE_VENUE, venueData.getName(), venueData.getPostalAddress(), venueData.getLatitude(), venueData.getLongitude(), venueData.getLocationHint(), 1, venueId);
			logger.info("Updated venue (ID = " + venueId + ")");
			return eventId;
		} catch (IncorrectResultSizeDataAccessException e) {
			// insert if it doesn't exist
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreatorFactory(INSERT_EVENT, new int[] {VARCHAR, VARCHAR, VARCHAR, TIMESTAMP, TIMESTAMP, VARCHAR, BIGINT})
					.newPreparedStatementCreator(new Object[] {eventData.getName(), eventData.getDescription(), eventData.getTimeZone(), eventData.getFirstDay(), eventData.getLastDay(), eventData.getAbbreviation(), eventData.getMemberGroupId()}), keyHolder);
			Number eventId = keyHolder.getKey();
			logger.info("Created event (ID = " + eventId + ")");
			jdbcTemplate.update(new PreparedStatementCreatorFactory(INSERT_VENUE, new int[] {VARCHAR, VARCHAR, DOUBLE, DOUBLE, VARCHAR, BIGINT})
					.newPreparedStatementCreator(new Object[] {venueData.getName(), venueData.getPostalAddress(), venueData.getLatitude(), venueData.getLongitude(), venueData.getLocationHint(), 1}), keyHolder);
			Number venueId = keyHolder.getKey();
			logger.info("Created vanue (ID = " + venueId + ")");
			jdbcTemplate.update(INSERT_EVENT_VENUE, eventId, venueId);
			jdbcTemplate.update(INSERT_EXTERNAL_EVENT, eventId, eventData.getSourceId(), eventData.getSource(), new Date());
			return eventId.longValue();
		}
	}
	
	@Transactional
	public long loadLeader(LeaderData leaderData) {
		try {
			// update if it already exists
			int leaderId = jdbcTemplate.queryForInt(SELECT_EXTERNAL_LEADER, leaderData.getSourceId(), leaderData.getSource());
			jdbcTemplate.update(UPDATE_LEADER, leaderData.getName(), leaderData.getBio(), leaderData.getPersonalUrl(), leaderData.getTwitterId(), leaderId);
			logger.info("Updated leader (ID = " + leaderId + ")");
			return leaderId;
		} catch (IncorrectResultSizeDataAccessException e) {
			// insert if it doesn't exist
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreatorFactory(INSERT_LEADER, new int[] {VARCHAR, VARCHAR, VARCHAR, VARCHAR})
					.newPreparedStatementCreator(new Object[] {leaderData.getName(), leaderData.getBio(), leaderData.getPersonalUrl(), leaderData.getTwitterId()}), keyHolder);
			long leaderId = keyHolder.getKey().longValue();
			jdbcTemplate.update(INSERT_EXTERNAL_LEADER, leaderId, leaderData.getSourceId(), leaderData.getSource(), new Date());
			logger.info("Created leader (ID = " + leaderId + ")");
			return leaderId;
		}
	};
	
	@Transactional
	public long loadTimeSlot(TimeSlotData timeSlotData) {
		try {
			// update if it already exists
			int timeSlotId = jdbcTemplate.queryForInt(SELECT_EXTERNAL_TIMESLOT, timeSlotData.getSourceId(), timeSlotData.getSource());
			jdbcTemplate.update(UPDATE_TIMESLOT, timeSlotData.getEventId(), timeSlotData.getLabel(), timeSlotData.getStartTime(), timeSlotData.getEndTime(), timeSlotId);
			logger.info("Updated timeslot (ID = " + timeSlotId + ")");
			return timeSlotId;
		} catch (IncorrectResultSizeDataAccessException e) {
			// insert if it doesn't exist
			KeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreatorFactory(INSERT_TIMESLOT, new int[] {BIGINT, VARCHAR, TIMESTAMP, TIMESTAMP})
				.newPreparedStatementCreator(new Object[] {timeSlotData.getEventId(), timeSlotData.getLabel(), timeSlotData.getStartTime(), timeSlotData.getEndTime()}), keyHolder);
			long timeSlotId = keyHolder.getKey().longValue();
			jdbcTemplate.update(INSERT_EXTERNAL_TIMESLOT, timeSlotId, timeSlotData.getSourceId(), timeSlotData.getSource(), new Date());
			logger.info("Created timeslot (ID = " + timeSlotId + ")");
			return timeSlotId;			
		}
	}

	@Transactional
	public int loadEventSession(EventSessionData sessionData) {
		try {
			long[] sessionKey = jdbcTemplate.queryForObject(SELECT_EXTERNAL_SESSION,
					new RowMapper<long[]>() {
						public long[] mapRow(ResultSet rs, int rowNum) throws SQLException {
							return new long[] {rs.getLong("event"), rs.getInt("sessionId")};
						}
					}, sessionData.getSource(), sessionData.getSourceId());
			jdbcTemplate.update(UPDATE_SESSION, 
					sessionData.getTitle(), sessionData.getDescription(), sessionData.getHashtag(), sessionData.getVenue(), sessionData.getTimeslot(), sessionKey[0], sessionKey[1]);
			logger.info("Updated session (EVENT = " + sessionKey[0] + ", ID = " + sessionKey[1] + ")");
			return 1;
		} catch (IncorrectResultSizeDataAccessException e) {
			// insert if it doesn't exist
			int newSessionId = jdbcTemplate.queryForInt("select max(id) from EventSession where event=?", sessionData.getEvent()) + 1;
			
			
			jdbcTemplate.update(INSERT_SESSION, sessionData.getEvent(), newSessionId, sessionData.getTitle(), sessionData.getDescription(), sessionData.getHashtag(), sessionData.getVenue(), sessionData.getTimeslot());
			jdbcTemplate.update(INSERT_EXTERNAL_SESSION, sessionData.getEvent(), newSessionId, sessionData.getSource(), sessionData.getSourceId(), new Date());
			
			List<Long> leaderIds = sessionData.getLeaderIds();
			int rank = 1;
			for (Long leaderId : leaderIds) {
				jdbcTemplate.update(INSERT_SESSION_LEADER, sessionData.getEvent(), newSessionId, leaderId, rank++);
			}
			
			logger.info("Created session (EVENT = " + sessionData.getEvent() + ", ID = " + newSessionId + ")");
			return newSessionId;
		}
	}

	private static final String SELECT_EXTERNAL_EVENT = "select event from ExternalEvent where sourceId = ? and source = ?";
	private static final String INSERT_EVENT = "insert into Event (title, description, timeZone, startTime, endTime, slug, memberGroup) values (?, ?, ?, ?, ?, ?, ?)";
	private static final String INSERT_VENUE = "insert into Venue (name, postalAddress, latitude, longitude, locationHint, createdBy) values (?, ?, ?, ?, ?, ?)";
	private static final String INSERT_EVENT_VENUE = "insert into EventVenue (event, venue) values (?, ?)";
	private static final String UPDATE_VENUE = "update venue set name = ?, postalAddress = ?, latitude = ?, longitude = ?, locationHint = ?, createdBy = ? where id = ?";
	private static final String SELECT_EVENT_VENUE = "select venue from EventVenue where event=?";
	private static final String INSERT_EXTERNAL_EVENT = "insert into ExternalEvent (event, sourceId, source, lastUpdated) values (?, ?, ?, ?)";
	private static final String UPDATE_EVENT = "update Event set title = ?, description = ?, timeZone = ?, startTime = ?, endTime = ?, slug = ? where id = ?";
	private static final String INSERT_EXTERNAL_LEADER = "insert into ExternalLeader (leader, sourceId, source, lastUpdated) values (?, ?, ?, ?)";
	private static final String INSERT_LEADER = "insert into Leader (name, bio, personalUrl, twitterUsername) values (?, ?, ?, ?)";
	private static final String UPDATE_LEADER = "update Leader set name=?, bio=?, personalUrl=?, twitterUsername=? where id=?";
	private static final String SELECT_EXTERNAL_LEADER = "select leader from ExternalLeader where sourceId = ? and source = ?";
	private static final String INSERT_TIMESLOT = "insert into EventTimeSlot (event, label, startTime, endTime) values (?, ?, ?, ?)";
	private static final String INSERT_EXTERNAL_TIMESLOT = "insert into ExternalEventTimeSlot (timeSlot, sourceId, source, lastUpdated) values (?, ?, ?, ?)";
	private static final String SELECT_EXTERNAL_TIMESLOT = "select timeSlot from ExternalEventTimeSlot where sourceId = ? and source = ?";
	private static final String UPDATE_TIMESLOT = "update EventTimeSlot set event=?, label=?, startTime=?, endTime=? where id=?";
	private static final String INSERT_SESSION = "insert into EventSession (event, id, title, description, hashtag, venue, timeslot) values (?, ?, ?, ?, ?, ?, ?)";
	private static final String SELECT_EXTERNAL_SESSION = "select event, sessionId from ExternalEventSession where source=? and sourceId=?";
	private static final String UPDATE_SESSION = "update EventSession set title=?, description=?, hashtag=?, venue=?, timeslot=? where event=? and id=?";
	private static final String INSERT_EXTERNAL_SESSION = "insert into ExternalEventSession (event, sessionId, source, sourceId, lastUpdated) values (?, ?, ?, ?, ?)";
	private static final String INSERT_SESSION_LEADER = "insert into EventSessionLeader (event, session, leader, rank) values (?, ?, ?, ?)";


}