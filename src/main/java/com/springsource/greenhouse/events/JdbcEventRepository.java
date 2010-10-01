package com.springsource.greenhouse.events;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.database.MultipleRowMapper;
import com.springsource.greenhouse.utils.Location;
import com.springsource.greenhouse.utils.ResourceReference;
import com.springsource.greenhouse.utils.SubResourceReference;

@Repository
public class JdbcEventRepository implements EventRepository {

	private JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcEventRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Event> findUpcomingEvents(Long afterMillis) {
		return jdbcTemplate.query(SELECT_UPCOMING_EVENTS, eventMapper, new Date(afterMillis != null ? afterMillis : System.currentTimeMillis()));
	}
	
	public Event findEventBySlug(String groupSlug, Integer year, Integer month, String slug) {
		return jdbcTemplate.queryForObject(SELECT_EVENT_BY_SLUG, eventMapper, groupSlug, year, month, slug);
	}

	public String findEventSearchString(Long eventId) {
		return jdbcTemplate.queryForObject("select g.hashtag from Event e, MemberGroup g where e.id = ? and e.memberGroup = g.id", String.class, eventId);
	}

	public String findSessionSearchString(Long eventId, Integer sessionId) {
		return jdbcTemplate.queryForObject("select (select g.hashtag from Event e, MemberGroup g where e.id = ? and e.memberGroup = g.id) || ' ' || hashtag from EventSession where event = ? and id = ?", String.class, eventId, eventId, sessionId);
	}

	public List<EventSession> findSessionsOnDay(Long eventId, LocalDate day, Long attendeeId) {
		DateTime dayStart = day.toDateTimeAtStartOfDay(DateTimeZone.UTC);
		DateTime dayEnd = dayStart.plusDays(1);
		return jdbcTemplate.query(SELECT_SESSIONS_ON_DAY, eventSessionsExtractor, attendeeId, eventId, dayStart.toDate(), dayEnd.toDate());
	}

	public List<EventSession> findEventFavorites(Long eventId, Long attendeeId) {
		return jdbcTemplate.query(SELECT_EVENT_FAVORITES, eventSessionsExtractor, attendeeId, eventId);
	}

	public List<EventSession> findAttendeeFavorites(Long eventId, Long attendeeId) {
		return jdbcTemplate.query(SELECT_ATTENDEE_FAVORITES, eventSessionsExtractor, attendeeId, eventId);
	}

	@Transactional
	public boolean toggleFavorite(Long eventId, Integer sessionId, Long attendeeId) {
		boolean favorite = jdbcTemplate.queryForObject("select exists(select 1 from EventSessionFavorite where event = ? and session = ? and attendee = ?)", Boolean.class, eventId, sessionId, attendeeId);
		if (favorite) {
			jdbcTemplate.update("delete from EventSessionFavorite where event = ? and session = ? and attendee = ?", eventId, sessionId, attendeeId);
		} else {
			jdbcTemplate.update("insert into EventSessionFavorite (event, session, attendee) values (?, ?, ?)", eventId, sessionId, attendeeId);
		}
		return !favorite;
	}

	@Transactional
	public void rate(Long eventId, Integer sessionId, Long attendeeId, Short value, String comment) {
		boolean rated = jdbcTemplate.queryForObject("select exists(select 1 from EventSessionRating where event = ? and session = ? and attendee = ?)", Boolean.class, eventId, sessionId, attendeeId);
		if (rated) {
			jdbcTemplate.update("update EventSessionRating set rating = ?, comment = ? where event = ? and session = ? and attendee = ?", value, comment, eventId, sessionId, attendeeId);			
		} else {
			jdbcTemplate.update("insert into EventSessionRating (event, session, attendee, rating, comment) values (?, ?, ?, ?, ?)", eventId, sessionId, attendeeId, value, comment);			
		}
		Float rating = jdbcTemplate.queryForObject("select round(avg(cast(rating as double)) * 2, 0) / 2 from EventSessionRating where event = ? and session = ? group by event, session", Float.class, eventId, sessionId);
		jdbcTemplate.update("update EventSession set rating = ? where event = ? and id = ?", rating, eventId, sessionId);
	}
	
	// internal helpers
	
	private RowMapper<Event> eventMapper = new RowMapper<Event>() {
		public Event mapRow(ResultSet rs, int row) throws SQLException {
			return new MultipleRowMapper<Event, Long, Venue>() {
				protected Long mapId(ResultSet rs) throws SQLException {
					return rs.getLong("id");
				}
				protected Event mapRoot(Long id, ResultSet rs) throws SQLException {
					return new Event(id, rs.getString("title"), DateTimeZone.forID(rs.getString("timeZone")), new DateTime(rs.getTimestamp("startTime"), DateTimeZone.UTC), new DateTime(rs.getTimestamp("endTime"), DateTimeZone.UTC),
							rs.getString("slug"), rs.getString("description"), rs.getString("hashtag"), new ResourceReference<String>(rs.getString("groupSlug"), rs.getString("groupName")));
				}
				protected void addChild(Event event, ResultSet rs) throws SQLException {
					event.addVenue(new Venue(rs.getLong("venueId"), rs.getString("venueName"), rs.getString("venuePostalAddress"), 
							new Location(rs.getDouble("venueLatitude"), rs.getDouble("venueLongitude")), rs.getString("venueLocationHint")));
				}
			}.map(rs);
		}
	};

	private ResultSetExtractor<List<EventSession>> eventSessionsExtractor = new ResultSetExtractor<List<EventSession>>() {
		public List<EventSession> extractData(ResultSet rs) throws SQLException {
			return new MultipleRowMapper<EventSession, Integer, EventSessionLeader>() {
				protected Integer mapId(ResultSet rs) throws SQLException {
					return rs.getInt("id");
				}
				protected EventSession mapRoot(Integer id, ResultSet rs) throws SQLException {
					return new EventSession(id, rs.getString("title"), new DateTime(rs.getTimestamp("startTime"), DateTimeZone.UTC), new DateTime(rs.getTimestamp("endTime"), DateTimeZone.UTC),
							rs.getString("description"), rs.getString("hashtag"), rs.getFloat("rating"), new SubResourceReference<Long, Integer>(rs.getLong("venue"), rs.getInt("room"), rs.getString("roomName")), rs.getBoolean("favorite"));
				}
				protected void addChild(EventSession session, ResultSet rs) throws SQLException {
					session.addLeader(new EventSessionLeader(rs.getString("firstName"), rs.getString("lastName")));					
				}
			}.mapInto(new ArrayList<EventSession>(), rs);
		}
	};
	
	private static final String SELECT_EVENT = "select e.id, e.title, e.timeZone, e.startTime, e.endTime, e.slug, e.description, g.hashtag, g.slug as groupSlug, g.name as groupName, " + 
		"v.id as venueId, v.name as venueName, v.postalAddress as venuePostalAddress, v.latitude as venueLatitude, v.longitude as venueLongitude, v.locationHint as venueLocationHint from Event e " + 
		"inner join MemberGroup g on e.memberGroup = g.id " + 
		"inner join EventVenue ev on e.id = ev.event " + 
		"inner join Venue v on ev.venue = v.id";
	
	private static final String SELECT_UPCOMING_EVENTS = SELECT_EVENT + " where e.endTime > ? order by e.startTime";

	private static final String SELECT_EVENT_BY_SLUG = SELECT_EVENT + " where g.slug = ? and extract(year from e.startTime) = ? and extract(month from e.startTime) = ? and e.slug = ?";

	private static final String SELECT_FROM_EVENT_SESSION = "select s.id, s.title, s.startTime, s.endTime, s.description, s.hashtag, s.rating, s.venue, s.room, r.name as roomName, (f.attendee is not null) as favorite, m.firstName, m.lastName from EventSession s ";
	
	private static final String SELECT_SESSIONS_ON_DAY = SELECT_FROM_EVENT_SESSION +
		"inner join VenueRoom r on s.venue = r.venue and s.room = r.id " +
		"left outer join EventSessionFavorite f on s.event = f.event and s.id = f.session and f.attendee = ? " +
		"inner join EventSessionLeader l on s.event = l.event and s.id = l.session " +
		"inner join Member m on l.leader = m.id where s.event = ? and s.startTime >= ? and s.endTime <= ? " +
		"order by s.startTime, l.rank";

	private static final String SELECT_EVENT_FAVORITES = SELECT_FROM_EVENT_SESSION +
		"inner join (select top 10 session, count(*) as favoriteCount from EventSessionFavorite group by session) top on s.id = top.session " +
		"inner join VenueRoom r on s.venue = r.venue and s.room = r.id " +
		"left outer join EventSessionFavorite f on s.event = f.event and s.id = f.session and f.attendee = ? " +
		"inner join EventSessionLeader l on s.event = l.event and s.id = l.session " +
		"inner join Member m on l.leader = m.id where s.event = ? " +
		"order by top.favoriteCount desc, l.rank";

	private static final String SELECT_ATTENDEE_FAVORITES = SELECT_FROM_EVENT_SESSION +
		"inner join VenueRoom r on s.venue = r.venue and s.room = r.id " +
		"inner join EventSessionFavorite f on s.event = f.event and s.id = f.session and f.attendee = ? " +	
		"inner join EventSessionLeader l on s.event = l.event and s.id = l.session " +
		"inner join Member m on l.leader = m.id where s.event = ? " +
		"order by f.rank, l.rank";
}