package com.springsource.greenhouse.events;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
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
		LocalDate day = new LocalDate();
		Date startInstant = day.toDateTimeAtStartOfDay(DateTimeZone.UTC).toDate();
		Date endInstant = day.plusDays(1).toDateTimeAtStartOfDay(DateTimeZone.UTC).toDate();
		return jdbcTemplate.query("select s.code, s.title, s.startTime, s.endTime, s.description, m.firstName, m.lastName from EventSession s inner join EventSessionLeader l on s.code = l.session inner join Member m on l.leader = m.id where s.startTime > ? and s.endTime < ?", eventSessionsExtractor, startInstant, endInstant);
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

	private ResultSetExtractor<List<EventSession>> eventSessionsExtractor = new ResultSetExtractor<List<EventSession>>() {
		public List<EventSession> extractData(ResultSet rs) throws SQLException, DataAccessException {
			List<EventSession> sessions = new ArrayList<EventSession>();
			EventSession session = null;
			Short previousCode = null;
			while (rs.next()) {
				Short code = rs.getShort("code");
				if (!code.equals(previousCode)) {
					session = new EventSession(code, rs.getString("title"), rs.getDate("startTime"), rs.getDate("endTime"), rs.getString("description"));
					sessions.add(session);
				}
				session.addLeader(new EventSessionLeader(rs.getString("firstName"), rs.getString("lastName")));				
				previousCode = code;
			}
			return sessions;			
		}
	};

	private static final String SELECT_EVENT = 
		"select id, title, startDate, endDate, location, description from Event";

}
