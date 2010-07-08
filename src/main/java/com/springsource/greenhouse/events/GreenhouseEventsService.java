package com.springsource.greenhouse.events;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.springsource.greenhouse.events.Event;

public class GreenhouseEventsService {

	private JdbcTemplate jdbcTemplate;

	@Inject
	public GreenhouseEventsService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<Event> getEvents() {
		
		return jdbcTemplate.query(
				"select id, " +
				"title, " +
				"description, " +
				"startDate, " +
				"endDate, " +
				"startTime, " +
				"endTime, " +
				"hashtag, " +
				"createdByUserId, " +
				"modifiedByUserId, " +
				"lastModified " +
				"from Event " +
				"order by startDate, startTime desc", 
				eventMapper);		
	}
	
	public Event getEventById(long eventId) {
		
		Event event = jdbcTemplate.queryForObject(
				"select id, " +
				"title, " +
				"description, " +
				"startDate, " +
				"endDate, " +
				"startTime, " +
				"endTime, " +
				"hashtag, " +
				"createdByUserId, " +
				"modifiedByUserId, " +
				"lastModified " +
				"from Event " +
				"where id = ? " +
				"order by startDate, startTime desc", 
				eventMapper,
				eventId);
		
		if (event != null) {
			event.setSessions(this.getSessionsByEventId(event.getId()));
		}
		
		return event;
	}
	
	public List<EventSession> getSessionsByEventId(long eventId) {
		return jdbcTemplate.query(
				"select id, " +
				"title, " +
				"description, " +
				"sessionDate, " +
				"startTime, " +
				"endTime, " +
				"hashtag, " +
				"createdByUserId, " +
				"modifiedByUserId, " +
				"lastModified " +
				"from EventSession " +
				"where eventId = ? " +
				"order by sessionDate, startTime desc", 
				eventSessionMapper, 
				eventId);
	}

	private RowMapper<Event> eventMapper = new RowMapper<Event>() {
		public Event mapRow(ResultSet rs, int row) throws SQLException {
			Event event = new Event();
			event.setId(rs.getLong("id"));
			event.setTitle(rs.getString("title"));
			event.setDescription(rs.getString("description"));
			event.setStartDate(rs.getDate("startDate"));
			event.setEndDate(rs.getDate("endDate"));
			event.setStartTime(rs.getTime("startTime"));
			event.setEndTime(rs.getTime("endTime"));
			event.setHashtag(rs.getString("hashtag"));
			event.setCreatedByUserId(rs.getLong("createdByUserId"));
			event.setModifiedByUserId(rs.getLong("modifiedByUserId"));
			event.setLastUpdated(rs.getTimestamp("lastModified"));
			return event;
		}
	};
	
	private RowMapper<EventSession> eventSessionMapper = new RowMapper<EventSession>() {
		public EventSession mapRow(ResultSet rs, int row) throws SQLException {
			EventSession session = new EventSession();
			session.setId(rs.getLong("id"));
			session.setTitle(rs.getString("title"));
			session.setDescription(rs.getString("description"));
			session.setSessionDate(rs.getDate("sessionDate"));
			session.setStartTime(rs.getTime("startTime"));
			session.setEndTime(rs.getTime("endTime"));
			session.setHashtag(rs.getString("hashtag"));
			session.setCreatedByUserId(rs.getLong("createdByUserId"));
			session.setModifiedByUserId(rs.getLong("modifiedByUserId"));
			session.setLastUpdated(rs.getTimestamp("lastModified"));
			return session;
		}
	};
	
}
