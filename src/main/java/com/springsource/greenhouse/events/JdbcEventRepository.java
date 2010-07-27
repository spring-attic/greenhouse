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
public class JdbcEventRepository implements EventRepository {

	private JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcEventRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Event> findUpcomingEvents() {		
		return jdbcTemplate.query(SELECT_EVENT + " where endDate > ? order by startDate", eventMapper, new Date());		
	}

	public String getEventSearchString(Long eventId) {
		return jdbcTemplate.queryForObject("select g.searchString from Event e, MemberGroup g where e.id = ? and e.memberGroup = g.id", String.class, eventId);
	}

	public String getEventSessionSearchString(Long eventId, Short sessionCode) {
		return getEventSearchString(eventId) + "-" + sessionCode;
	}

	public List<EventSession> findTodaysSessions(Long eventId) {
		return null;
	}

	private RowMapper<Event> eventMapper = new RowMapper<Event>() {
		public Event mapRow(ResultSet rs, int row) throws SQLException {
			Event event = new Event();
			event.setId(rs.getLong("id"));
			event.setTitle(rs.getString("title"));
			event.setStartDate(rs.getTimestamp("startDate"));
			event.setEndDate(rs.getTimestamp("endDate"));
			event.setLocation(rs.getString("location"));
			event.setDescription(rs.getString("description"));
			return event;
		}
	};
	
	private static final String SELECT_EVENT = 
		"select id, title, startDate, endDate, location, description from Event";

}
