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
	private static final String SELECT_MEMBER_GROUP =
			"select id, publicId, name, description, hashtag from MemberGroup";
	private static final String SELECT_EVENT = 
			"select id, publicId, title, description, startTime, endTime, location, memberGroup, hashtag from Event";
	private static final String SELECT_SESSION = 
			"select code, title, description, startTime, endTime, speaker, event, track, hashtag from EventSession";
	private static final String SELECT_TRACK =
			"select id, name, description";

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
		event.setSessions(this.findSessionsByEventId(event.getId()));
		return event;
	}
	
	public List<EventSession> findSessionsByEventId(long eventId) {
		return jdbcTemplate.query(SELECT_SESSION + " where event = ? order by startTime", eventSessionMapper, eventId);
	}
	
	public List<EventTrack> getTracksByEventId(long eventId) {
		return jdbcTemplate.query(SELECT_TRACK + " where event = ? order by id", eventTrackMapper, eventId);
	}
	
	public Event findEventByPublicId(String eventName) {
		Event event = jdbcTemplate.queryForObject(SELECT_EVENT + " where publicId = ?", eventMapper, eventName);
		event.setSessions(this.findSessionsByEventId(event.getId()));		
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
	
	private RowMapper<EventSession> eventSessionMapper = new RowMapper<EventSession>() {
		public EventSession mapRow(ResultSet rs, int row) throws SQLException {
			EventSession session = new EventSession();
			session.setCode(rs.getString("code"));
			session.setTitle(rs.getString("title"));
			session.setDescription(rs.getString("description"));
			session.setStartTime(rs.getTimestamp("startTime"));
			session.setEndTime(rs.getTimestamp("endTime"));
			session.setHashtag(rs.getString("hashtag"));
			return session;
		}
	};

	private RowMapper<EventTrack> eventTrackMapper = new RowMapper<EventTrack>() {
		public EventTrack mapRow(ResultSet rs, int row) throws SQLException {
			EventTrack track = new EventTrack();
			track.setIdentity(rs.getLong("id"));
			track.setName(rs.getString("name"));
			track.setDescription(rs.getString("description"));
			return track;
		}
	};
}
