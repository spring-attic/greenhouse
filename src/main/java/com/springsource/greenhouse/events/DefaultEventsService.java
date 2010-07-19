package com.springsource.greenhouse.events;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class DefaultEventsService implements EventsService {
	private static final String SELECT_EVENT = 
			"select id, publicId, title, description, startTime, endTime, location, memberGroup, hashtag from Event";

	private JdbcTemplate jdbcTemplate;

	@Inject
	public DefaultEventsService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<Event> findEventsAfter(Date afterDate) {		
		return jdbcTemplate.query(SELECT_EVENT + " where endTime > ? order by startTime",	eventMapper, afterDate);		
	}
	
	public Event findEventById(long eventId) {		
		Event event = jdbcTemplate.queryForObject(SELECT_EVENT + " where id=? order by startTime", 
				eventMapper, eventId);
		return event;
	}
	
	public Event findEventByPublicId(String eventName) {
		Event event = jdbcTemplate.queryForObject(SELECT_EVENT + " where publicId = ?", eventMapper, eventName);
		return event;
	}
	
	private RowMapper<Event> eventMapper = new RowMapper<Event>() {
		public Event mapRow(ResultSet rs, int row) throws SQLException {
			Event event = new Event();
			event.setId(rs.getLong("id"));
			event.setFriendlyId(rs.getString("publicId"));
			event.setTitle(rs.getString("title"));
			event.setDescription(rs.getString("description"));
			event.setLocation(rs.getString("location"));
			event.setStartTime(rs.getTimestamp("startTime"));
			event.setEndTime(rs.getTimestamp("endTime"));
			event.setHashtag(rs.getString("hashtag"));
			return event;
		}
	};
}
