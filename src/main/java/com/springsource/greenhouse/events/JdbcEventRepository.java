package com.springsource.greenhouse.events;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JdbcEventRepository implements EventRepository {

	private JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcEventRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Event> findUpcomingEvents() {		
		return jdbcTemplate.query(SELECT_UPCOMING_EVENTS, eventMapper, new Date());
	}
	
	public Event findEventByName(String group, Integer year, Integer month, String name) {
		return jdbcTemplate.queryForObject(SELECT_EVENT_BY_NAME, eventMapper, group, year, month, name);
	}

	public String findEventHashtag(Long eventId) {
		return jdbcTemplate.queryForObject("select g.hashtag from Event e, MemberGroup g where e.id = ? and e.memberGroup = g.id", String.class, eventId);
	}

	public String findSessionHashtag(Long eventId, Short sessionNumber) {
		return jdbcTemplate.queryForObject("select hashtag from EventSession where event = ? and number = ?", String.class, eventId, sessionNumber);
	}

	public List<EventSession> findTodaysSessions(Long eventId, Long memberId) {
		return findSessionsOnDay(eventId, new LocalDate(), memberId);
	}
	
	public List<EventSession> findSessionsOnDay(Long eventId, LocalDate day, Long attendeeId) {
		Date startInstant = day.toDateTimeAtStartOfDay(DateTimeZone.UTC).toDate();
		Date endInstant = day.toDateTimeAtStartOfDay(DateTimeZone.UTC).plusDays(1).toDate();
		return jdbcTemplate.query(SELECT_SESSIONS_ON_DAY, eventSessionsExtractor, attendeeId, eventId, startInstant, endInstant);
	}

	public List<EventSession> findFavoriteSessions(Long eventId, Long attendeeId) {
		return jdbcTemplate.query(SELECT_FAVORITE_SESSIONS, eventSessionsExtractor, attendeeId, eventId);
	}

	public List<EventFavorite> findFavorites(Long eventId) {
		List<EventFavorite> favorites = Collections.emptyList();
		// TODO complete implementation
		return favorites;
	}

	@Transactional
	public boolean toggleFavorite(Long eventId, Short sessionNumber, Long attendeeId) {
		boolean favorite = jdbcTemplate.queryForObject("select exists(select 1 from EventSessionFavorite where event = ? and session = ? and attendee = ?)", Boolean.class, eventId, sessionNumber, attendeeId);
		if (favorite) {
			jdbcTemplate.update("delete from EventSessionFavorite where event = ? and session = ? and attendee = ?", eventId, sessionNumber, attendeeId);
		} else {
			jdbcTemplate.update("insert into EventSessionFavorite (event, session, attendee) values (?, ?, ?)", eventId, sessionNumber, attendeeId);
		}
		return !favorite;
	}

	public void updateRating(Long eventId, Short sessionNumber, Long attendeeId, Short value) {
		// TODO complete implementation
	}
	
	// internal helpers
	
	private RowMapper<Event> eventMapper = new RowMapper<Event>() {
		public Event mapRow(ResultSet rs, int row) throws SQLException {
			return new Event(rs.getLong("id"), rs.getString("title"), new DateTime(rs.getTimestamp("startTime"), DateTimeZone.UTC), new DateTime(rs.getTimestamp("endTime"), DateTimeZone.UTC),
					rs.getString("location"), rs.getString("description"), rs.getString("name"), rs.getString("hashtag"), rs.getString("groupName"), rs.getString("groupProfileKey"));
		}
	};

	private ResultSetExtractor<List<EventSession>> eventSessionsExtractor = new ResultSetExtractor<List<EventSession>>() {
		public List<EventSession> extractData(ResultSet rs) throws SQLException, DataAccessException {
			List<EventSession> sessions = new ArrayList<EventSession>();
			EventSession session = null;
			Short previousNumber = null;
			while (rs.next()) {
				Short number = rs.getShort("number");
				if (!number.equals(previousNumber)) {
					// TODO map favorite status
					session = new EventSession(number, rs.getString("title"), new DateTime(rs.getTimestamp("startTime"), DateTimeZone.UTC), new DateTime(rs.getTimestamp("endTime"), DateTimeZone.UTC),
							rs.getString("description"), rs.getString("hashtag"), rs.getFloat("rating"), rs.getBoolean("favorite"));
					sessions.add(session);
				}
				session.addLeader(new EventSessionLeader(rs.getString("firstName"), rs.getString("lastName")));				
				previousNumber = number;
			}
			return sessions;			
		}
	};

	private static final String SELECT_UPCOMING_EVENTS = "select e.id, e.title, e.startTime, e.endTime, e.location, e.description, e.name, g.hashtag, g.name as groupName, g.profileKey as groupProfileKey from Event e " + 
		"inner join MemberGroup g on e.memberGroup = g.id where e.endTime > ? order by e.startTime";

	private static final String SELECT_EVENT_BY_NAME = "select e.id, e.title, e.startTime, e.endTime, e.location, e.description, e.name, g.hashtag, g.name as groupName, g.profileKey as groupProfileKey from Event e " + 
		"inner join MemberGroup g on e.memberGroup = g.id where g.profileKey = ? and extract(year from e.startTime) = ? and extract(month from e.startTime) = ? and e.name = ?";

	private static final String SELECT_SESSIONS_ON_DAY =
		"select s.number, s.title, s.startTime, s.endTime, s.description, s.hashtag, s.rating, (f.attendee is not null) as favorite, m.firstName, m.lastName from EventSession s " +
		"left outer join EventSessionFavorite f on s.event = f.event and s.number = f.session and f.attendee = ? " +	
		"inner join EventSessionLeader l on s.event = l.event and s.number = l.session " +
		"inner join Member m on l.leader = m.id where s.event = ? and s.startTime >= ? and s.endTime <= ?";

	private static final String SELECT_FAVORITE_SESSIONS =
		"select s.number, s.title, s.startTime, s.endTime, s.description, s.hashtag, s.rating, (true) as favorite, m.firstName, m.lastName from EventSession s " +
		"inner join EventSessionFavorite f on s.event = f.event and s.number = f.session and f.attendee = ? " +	
		"inner join EventSessionLeader l on s.event = l.event and s.number = l.session " +
		"inner join Member m on l.leader = m.id where s.event = ?";
}