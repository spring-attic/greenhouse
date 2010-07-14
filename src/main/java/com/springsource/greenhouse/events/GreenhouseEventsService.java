package com.springsource.greenhouse.events;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class GreenhouseEventsService {
	private static final String SELECT_EVENT = 
			"select id, title, description, startTime, endTime, location, hashtag from Event";
	private static final String SELECT_SESSION = 
			"select code, title, description, startTime, endTime, speaker, event, track, hashtag from EventSession";

	private JdbcTemplate jdbcTemplate;

	@Inject
	public GreenhouseEventsService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<Event> getEventsAfter(Date afterDate) {		
		return jdbcTemplate.query(SELECT_EVENT + " where endTime > ? order by startTime",	eventMapper, afterDate);		
	}
	
	public Event getEventById(long eventId) {		
		Event event = jdbcTemplate.queryForObject(SELECT_EVENT + " where id=? order by startTime", 
				eventMapper, eventId);
		
		if (event != null) {
			event.setSessions(this.getSessionsByEventId(event.getId()));
		}
		
		return event;
	}
	
	public List<EventSession> getSessionsByEventId(long eventId) {
		return jdbcTemplate.query(SELECT_SESSION + " where event = ? order by startTime", 
				eventSessionMapper, 
				eventId);
	}

	private RowMapper<Event> eventMapper = new RowMapper<Event>() {
		public Event mapRow(ResultSet rs, int row) throws SQLException {
			Event event = new Event();
			event.setId(rs.getLong("id"));
			event.setTitle(rs.getString("title"));
			event.setDescription(rs.getString("description"));
			event.setLocation(rs.getString("location"));
			event.setStartTime(rs.getTimestamp("startTime"));
			event.setEndTime(rs.getTimestamp("endTime"));
			event.setHashtag(rs.getString("hashtag"));
			return event;
		}
	};
	
	private RowMapper<EventSession> eventSessionMapper = new RowMapper<EventSession>() {
		public EventSession mapRow(ResultSet rs, int row) throws SQLException {
			EventSession session = new EventSession();
			session.setCode(rs.getString("code"));
			session.setTitle(rs.getString("title"));
			session.setDescription(rs.getString("description"));
			session.setStartTime(rs.getTimestamp("startTime"));
			session.setEndTime(rs.getTimestamp("endTime"));
			session.setHashtag(rs.getString("hashtag"));	
			// TODO: Add speaker, event, track
			return session;
		}
	};
	
}
