/*
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
package com.springsource.greenhouse.events;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.MutableDateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.JoinRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.utils.Location;
import com.springsource.greenhouse.utils.ResourceReference;
import com.springsource.greenhouse.utils.SubResourceReference;

/**
 * EventRepository implementation that stores Event data in a relational database using the JDBC API.
 * @author Keith Donald
 */
@Repository
public class JdbcEventRepository implements EventRepository {

	private final JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcEventRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Event> findUpcomingEvents(Long afterMillis) {
		return jdbcTemplate.query(SELECT_UPCOMING_EVENTS, eventMapper.list(), new Date(afterMillis != null ? afterMillis : System.currentTimeMillis()));
	}
	
	public Event findEventBySlug(String groupSlug, Integer year, Integer month, String slug) {
		return jdbcTemplate.queryForObject(SELECT_EVENT_BY_SLUG, eventMapper.single(), groupSlug, year, month, slug);
	}

	public String findEventSearchString(Long eventId) {
		return jdbcTemplate.queryForObject("select g.hashtag from Event e, MemberGroup g where e.id = ? and e.memberGroup = g.id", String.class, eventId);
	}

	public String findSessionSearchString(Long eventId, Integer sessionId) {
		return jdbcTemplate.queryForObject("select (select g.hashtag from Event e, MemberGroup g where e.id = ? and e.memberGroup = g.id) || ' ' || hashtag from EventSession where event = ? and id = ?", String.class, eventId, eventId, sessionId);
	}

	@Transactional
	public List<EventSession> findSessionsOnDay(Long eventId, LocalDate day, Long attendeeId) {
		DateTimeZone eventTimeZone = DateTimeZone.forID(jdbcTemplate.queryForObject("select timezone from Event where id = ?", String.class, eventId));
		DateTime dayStart = day.toDateTimeAtStartOfDay(eventTimeZone);
		DateTime dayEnd = dayStart.plusDays(1);
		return jdbcTemplate.query(SELECT_SESSIONS_ON_DAY, eventSessionMapper.list(), attendeeId, eventId, dayStart.toDate(), dayEnd.toDate());
	}

	public List<EventSession> findEventFavorites(Long eventId, Long attendeeId) {
		return jdbcTemplate.query(SELECT_EVENT_FAVORITES, eventSessionMapper.list(), eventId, attendeeId, eventId);
	}

	public List<EventSession> findAttendeeFavorites(Long eventId, Long attendeeId) {
		return jdbcTemplate.query(SELECT_ATTENDEE_FAVORITES, eventSessionMapper.list(), attendeeId, eventId);
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
	public Float rate(Long eventId, Integer sessionId, Long attendeeId, Rating rating) throws RatingPeriodClosedException {
		if (!isSessionEnded(eventId, sessionId)) {
			throw new RatingPeriodClosedException(eventId, sessionId);
		}
		boolean rated = jdbcTemplate.queryForObject("select exists(select 1 from EventSessionRating where event = ? and session = ? and attendee = ?)", Boolean.class, eventId, sessionId, attendeeId);
		if (rated) {
			jdbcTemplate.update("update EventSessionRating set rating = ?, comment = ? where event = ? and session = ? and attendee = ?", rating.getValue(), rating.getComment(), eventId, sessionId, attendeeId);			
		} else {
			jdbcTemplate.update("insert into EventSessionRating (event, session, attendee, rating, comment) values (?, ?, ?, ?, ?)", eventId, sessionId, attendeeId, rating.getValue(), rating.getComment());			
		}
		Float newAvgRating = jdbcTemplate.queryForObject("select round(avg(cast(rating as double)) * 2, 0) / 2 from EventSessionRating where event = ? and session = ? group by event, session", Float.class, eventId, sessionId);
		jdbcTemplate.update("update EventSession set rating = ? where event = ? and id = ?", newAvgRating, eventId, sessionId);
		return newAvgRating;
	}
	
	@Transactional
	public long addEvent() {
		return 0;
	}
   
	// internal helpers
	
	private boolean isSessionEnded(Long eventId, Integer sessionId) {
		Date endTime = jdbcTemplate.queryForObject("select ts.endTime from EventTimeSlot ts, EventSession s where s.event = ? and s.id = ? and ts.id = s.timeSlot", Date.class, eventId, sessionId);
		return new Date().after(endTime);
	}

	private final JoinRowMapper<Event, Long> eventMapper = new JoinRowMapper<Event, Long>() {
		protected Long mapId(ResultSet rs) throws SQLException {
			return rs.getLong("id");
		}
		protected Event mapRoot(Long id, ResultSet rs) throws SQLException {
			String eventTimeZone = rs.getString("timeZone");
			return new Event(id, rs.getString("title"), DateTimeZone.forID(eventTimeZone), adjustEventTimeToUTC(rs.getTimestamp("startTime"), eventTimeZone), adjustEventTimeToUTC(rs.getTimestamp("endTime"), eventTimeZone),
					rs.getString("slug"), rs.getString("description"), rs.getString("hashtag"), new ResourceReference<String>(rs.getString("groupSlug"), rs.getString("groupName")));
		}
		protected void addChild(Event event, ResultSet rs) throws SQLException {
			event.addVenue(new Venue(rs.getLong("venueId"), rs.getString("venueName"), rs.getString("venuePostalAddress"), 
					new Location(rs.getDouble("venueLatitude"), rs.getDouble("venueLongitude")), rs.getString("venueLocationHint")));
		}
	};

	private final JoinRowMapper<EventSession, Integer> eventSessionMapper = new JoinRowMapper<EventSession, Integer>() {
		protected Integer mapId(ResultSet rs) throws SQLException {
			return rs.getInt("id");
		}
		protected EventSession mapRoot(Integer id, ResultSet rs) throws SQLException {
			String eventTimeZone = "America/New_York"; // HACK: For now hard-code to S2GX 2012's value while sorting this out.			
			return new EventSession(id, rs.getString("title"), adjustEventTimeToUTC(rs.getTimestamp("startTime"), eventTimeZone), adjustEventTimeToUTC(rs.getTimestamp("endTime"), eventTimeZone),
					rs.getString("description"), rs.getString("hashtag"), rs.getFloat("rating"), new SubResourceReference<Long, Integer>(rs.getLong("venue"), rs.getInt("room"), rs.getString("roomName")), rs.getBoolean("favorite"));
		}
		protected void addChild(EventSession session, ResultSet rs) throws SQLException {
			session.addLeader(new EventSessionLeader(rs.getString("name")));					
		}
	};
	
	private static DateTime adjustEventTimeToUTC(Timestamp timestamp, String eventTimeZone) {
		MutableDateTime mutableDateTime = new DateTime(timestamp).toMutableDateTime();
		mutableDateTime.setZoneRetainFields(DateTimeZone.forID(eventTimeZone));
		DateTime utcAdjustedDateTime = mutableDateTime.toDateTime().toDateTime(DateTimeZone.UTC);
		return utcAdjustedDateTime;
	}

	private static final String SELECT_FROM_EVENT_SESSION = "select s.id, s.title, ts.startTime, ts.endTime, s.description, s.hashtag, s.rating, s.venue, s.room, r.name as roomName, (f.attendee is not null) as favorite, l.name from EventSession s ";
	
	private static final String SELECT_EVENT = "select e.id, e.title, e.timeZone, e.startTime, e.endTime, e.slug, e.description, g.hashtag, g.slug as groupSlug, g.name as groupName, " + 
		"v.id as venueId, v.name as venueName, v.postalAddress as venuePostalAddress, v.latitude as venueLatitude, v.longitude as venueLongitude, v.locationHint as venueLocationHint from Event e " + 
		"inner join MemberGroup g on e.memberGroup = g.id " + 
		"inner join EventVenue ev on e.id = ev.event " + 
		"inner join Venue v on ev.venue = v.id";
		
	private static final String SELECT_UPCOMING_EVENTS = SELECT_EVENT + " where e.endTime > ? order by e.startTime";

	private static final String SELECT_EVENT_BY_SLUG = SELECT_EVENT + " where g.slug = ? and extract(year from e.startTime) = ? and extract(month from e.startTime) = ? and e.slug = ?";

	private static final String SELECT_SESSIONS_ON_DAY = SELECT_FROM_EVENT_SESSION +
		"left outer join VenueRoom r on s.venue = r.venue and s.room = r.id " +
		"left outer join EventSessionFavorite f on s.event = f.event and s.id = f.session and f.attendee = ? " +
		"inner join EventSessionLeader sl on s.event = sl.event and s.id = sl.session " +
		"inner join Leader l on sl.leader = l.id " + 
		"inner join EventTimeSlot ts on ts.id = s.timeSlot " +
		"where s.event = ? and ts.startTime >= ? and ts.endTime <= ? " +
		"order by ts.startTime, s.id, sl.rank";

	private static final String SELECT_EVENT_FAVORITES = SELECT_FROM_EVENT_SESSION +
		"inner join (select top 10 session, count(*) as favoriteCount from EventSessionFavorite where event = ? group by session) top on s.id = top.session " +
		"left outer join VenueRoom r on s.venue = r.venue and s.room = r.id " +
		"left outer join EventSessionFavorite f on s.event = f.event and s.id = f.session and f.attendee = ? " +
		"inner join EventSessionLeader sl on s.event = sl.event and s.id = sl.session " +
		"inner join Leader l on sl.leader = l.id " + 
		"inner join EventTimeSlot ts on ts.id = s.timeSlot " +
		"where s.event = ? " +
		"order by top.favoriteCount desc, s.id, sl.rank";

	private static final String SELECT_ATTENDEE_FAVORITES = SELECT_FROM_EVENT_SESSION +
		"left outer join VenueRoom r on s.venue = r.venue and s.room = r.id " +
		"inner join EventSessionFavorite f on s.event = f.event and s.id = f.session and f.attendee = ? " +	
		"inner join EventSessionLeader sl on s.event = sl.event and s.id = sl.session " +
		"inner join Leader l on sl.leader = l.id " + 
		"inner join EventTimeSlot ts on ts.id = s.timeSlot " +
		"where s.event = ? " +
		"order by f.rank, s.id, sl.rank";
	
}