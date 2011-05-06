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

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.JoinRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.utils.GeoLocation;
import com.springsource.greenhouse.utils.Location;
import com.springsource.greenhouse.utils.ResourceReference;
import com.springsource.greenhouse.utils.SlugUtils;
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

	public EventTrack findTrackByCode(String trackcode, Long eventId) {
		return jdbcTemplate.queryForObject(SELECT_TRACK_BY_CODE, eventTrackMapper.single(), trackcode, eventId);
	}
	
	public EventSession findSessionById(Integer sessionId, Long eventId) {
		return jdbcTemplate.queryForObject(SELECT_SESSION_BY_ID, eventSessionMapper.single(), sessionId, eventId);
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
		return jdbcTemplate.query(SELECT_EVENT_FAVORITES, eventSessionMapper.list(), attendeeId, eventId);
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
	public String createEvent(EventForm form, Long accountId) {
		String slug = createSlug(form.getTitle());
		Long eventId = insertEvent(slug, form);
		VenueFields venueFields = form.getVenueFields();
		Long venueId = venueFields.getId();
		if (venueId == null) {
			venueId = insertVenue(venueFields, accountId);
		}
		jdbcTemplate.update(INSERT_EVENT_VENUE, eventId, venueId);		
		return slug;
	}

	@Transactional
	public void createSession(Long accountId, Event event, EventSessionForm form) {
		Long venue = jdbcTemplate.queryForLong(SELECT_EVENTVENUE_ID, event.getId());
		Long id = jdbcTemplate.queryForLong(SELECT_MAX_SESSION_ID, event.getId()) + 1;
		LeaderFields leaderFields = form.getLeaderFields();
		Long leaderId = leaderFields.getId();
		if (leaderId == null) {
			leaderId = insertLeader(leaderFields);
		}
		jdbcTemplate.update(INSERT_SESSION, event.getId(), id, form.getTitle(), form.getDescription(), form.getHashtag(),
				form.getStartTime(event.getTimeZone()).toDate(), form.getEndTime(event.getTimeZone()).toDate(), venue, form.getTrackCode());
		jdbcTemplate.update(INSERT_SESSION_LEADER , event.getId(), id, leaderId);
	}
	
	@Transactional
	public void createRoom(Long accountId, Event event, EventRoomForm form) {
		Long venue = jdbcTemplate.queryForLong(SELECT_EVENTVENUE_ID, event.getId());
		Long id = jdbcTemplate.queryForLong(SELECT_ROOM_ID, venue) + 1;
		jdbcTemplate.update(INSERT_ROOM, venue, id, form.getName(), form.getCapacity(), form.getLocationHint());
	}
	
	@Transactional
	public String createTrack(Long accountId, Event event, EventTrackForm form) throws DuplicateKeyException {
		String slug = event.getSlug();
		int chair = 1;
		try {
			jdbcTemplate.update(INSERT_TRACK, event.getId(), form.getCode(), form.getName(), form.getDescription(), chair);
		} catch (DuplicateKeyException e) {
			throw new DuplicateKeyException(form.getCode());
		}
		return slug;
	}
	
	public EventTrackForm getTrackForm(Long eventId, String trackcode) {
		return jdbcTemplate.queryForObject(SELECT_TRACK_FORM, trackFormMapper, eventId, trackcode);
	}
	
	public String updateTrack(Event event, EventTrackForm form, String trackcode) {
		jdbcTemplate.update(UPDATE_TRACK_FORM, form.getName(), trackcode, form.getDescription(), event.getId(), trackcode);
		jdbcTemplate.update(UPDATE_TRACK_ROOMS, form.getRoomID(), trackcode, event.getId());
		return trackcode;
	}
	
	public String[] selectSpeakerNames() {
		int num = jdbcTemplate.queryForInt(SELECT_NUM_LEADER);
		String[] names = new String[num];
		for (int i=1; i<=num; i++){
			names[i-1] = jdbcTemplate.queryForObject(SELECT_LEADER, String.class, i);
		}
		return names;
	}
	
	public String[] selectVenueNames() {
		int num = jdbcTemplate.queryForInt(SELECT_NUM_VENUE);
		String[] names = new String[num];
		for (int i=1; i<=num; i++){
			names[i-1] = jdbcTemplate.queryForObject(SELECT_VENUE, String.class, i);
		}
		return names;
	}
	
	public String[] selectVenueAddresses() {
		int num = jdbcTemplate.queryForInt(SELECT_NUM_VENUE);
		String[] adds = new String[num];
		for (int i=1; i<=num; i++){
			adds[i-1] = jdbcTemplate.queryForObject(SELECT_VENUE_ADDRESSES, String.class, i);
		}
		return adds;
	}
	
	public String[] selectVenueLocationHints() {
		int num = jdbcTemplate.queryForInt(SELECT_NUM_VENUE);
		String[] hints = new String[num];
		for (int i=1; i<=num; i++){
			hints[i-1] = jdbcTemplate.queryForObject(SELECT_VENUE_LOCATIONHINTS, String.class, i);
		}
		return hints;
	}
	
	public String[] selectRoomNames(Long venueID) {
		int num = jdbcTemplate.queryForInt(SELECT_NUM_ROOMS, venueID);
		String[] rooms = new String[num];
		for (int i=1; i<=num; i++){
			rooms[i-1] = jdbcTemplate.queryForObject(SELECT_ROOM_NAMES, String.class, i, venueID);
		}
		return rooms;
	}
	
	public List<EventTrack> selectEventTracks(Long eventId) {
		return jdbcTemplate.query(SELECT_EVENT_TRACKS, eventTrackMapper.list(), eventId);
	}
	
	public List<EventSession> selectEventSessions(Long eventId) {
		return jdbcTemplate.query(SELECT_EVENT_SESSIONS, eventSessionMapper.list(), eventId);
	}
	
	// internal helpers

	private Long insertEvent(String slug, EventForm form) {
		// TODO the group organizing the event is currently hard-coded
		int group = 1;		
		jdbcTemplate.update(INSERT_EVENT, form.getTitle(), slug, form.getDescription(), form.getStartTime().toDate(), form.getEndTime().toDate(), form.getTimeZone(), group);
		// TODO eliminate the extra query here
		return jdbcTemplate.queryForLong(SELECT_EVENT_ID);
	}
	
	private Long insertVenue(VenueFields venueFields, Long accountId) {
		Location location = getLocation(venueFields.getAddress());
		jdbcTemplate.update(INSERT_VENUE, venueFields.getName(), venueFields.getAddress(), location.getLatitude(), location.getLongitude(), venueFields.getLocationHint(), accountId);
		// TODO eliminate the extra query here
		return jdbcTemplate.queryForLong(SELECT_VENUE_ID);
	}

	private Long insertLeader(LeaderFields fields) {
		jdbcTemplate.update(INSERT_LEADER, fields.getName(), fields.getCompany(), fields.getCompanyTitle(), fields.getCompanyURL(), fields.getTwitterName());
		// TODO eliminate the extra query here
		return jdbcTemplate.queryForLong(SELECT_LEADER_ID);
	}
	
	private Location getLocation(String address) {
		try {
			GeoLocation location = GeoLocation.getGeoLocation(address);
			return new Location(location.toLatitude(), location.toLongitude());
		} catch (IOException e) {
			throw new RuntimeException("Unable to geocode address " + address);
		}		
	}
	
	private String createSlug(String eventName) {
		return SlugUtils.toSlug(eventName);
	}

	private boolean isSessionEnded(Long eventId, Integer sessionId) {
		Date endTime = jdbcTemplate.queryForObject("select endTime from EventSession where event = ? and id = ?", Date.class, eventId, sessionId);
		return new Date().after(endTime);
	}

	private final JoinRowMapper<Event, Long> eventMapper = new JoinRowMapper<Event, Long>() {
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
	};

	private final JoinRowMapper<EventSession, Integer> eventSessionMapper = new JoinRowMapper<EventSession, Integer>() {
		protected Integer mapId(ResultSet rs) throws SQLException {
			return rs.getInt("id");
		}
		protected EventSession mapRoot(Integer id, ResultSet rs) throws SQLException {
			return new EventSession(id, rs.getString("title"), new DateTime(rs.getTimestamp("startTime"), DateTimeZone.UTC), new DateTime(rs.getTimestamp("endTime"), DateTimeZone.UTC),
					rs.getString("description"), rs.getString("hashtag"), rs.getFloat("rating"), new SubResourceReference<Long, Integer>(rs.getLong("venue"), rs.getInt("room"), rs.getString("roomName")), rs.getBoolean("favorite"));
		}
		protected void addChild(EventSession session, ResultSet rs) throws SQLException {
			session.addLeader(new EventSessionLeader(rs.getString("name")));					
		}
	};
	
	private final JoinRowMapper<EventTrack, String> eventTrackMapper = new JoinRowMapper<EventTrack, String>() {
		protected String mapId(ResultSet rs) throws SQLException {
			return rs.getString("code");
		}
		protected EventTrack mapRoot(String code, ResultSet rs) throws SQLException {
			return new EventTrack(code, rs.getString("name"), rs.getString("description"));
		}
		protected void addChild(EventTrack track, ResultSet rs) throws SQLException {					
		}
	};
	
	
	private RowMapper<EventTrackForm> trackFormMapper = new RowMapper<EventTrackForm>() {
		public EventTrackForm mapRow(ResultSet rs, int rowNum) throws SQLException {
			EventTrackForm form = new EventTrackForm();
			form.setCode("code");
			form.setName(rs.getString("name"));
			form.setDescription(rs.getString("description"));
			return form;
		}
	};
	
	private static final String SELECT_EVENT = "select e.id, e.title, e.timeZone, e.startTime, e.endTime, e.slug, e.description, g.hashtag, g.slug as groupSlug, g.name as groupName, " + 
		"v.id as venueId, v.name as venueName, v.postalAddress as venuePostalAddress, v.latitude as venueLatitude, v.longitude as venueLongitude, v.locationHint as venueLocationHint from Event e " + 
		"inner join MemberGroup g on e.memberGroup = g.id " + 
		"inner join EventVenue ev on e.id = ev.event " + 
		"inner join Venue v on ev.venue = v.id";
	
	private static final String INSERT_EVENT = "insert into event (Title, slug, description, starttime, endtime, TimeZone, membergroup) values (?, ?, ?, ?, ?, ?, ?)";
	
	private static final String INSERT_VENUE = "insert into venue (Name, postaladdress, latitude, longitude, locationhint, createdby) values (?, ?, ?, ?, ?, ?)";
	
	private static final String INSERT_TRACK = "INSERT INTO EVENTTRACK(EVENT, CODE, NAME, DESCRIPTION, CHAIR) VALUES (?, ?, ?, ?, ?)";
	
	private static final String INSERT_ROOM = "insert into venueroom (venue, id, name, capacity, locationhint) values (?, ?, ?, ?, ?)";
	
	private static final String INSERT_SESSION = "insert into eventsession (event, id, title, description, hashtag, starttime, endtime, venue, track) values (?, ?, ?,?, ?, ?, ?, ?, ?)";
		
	private static final String INSERT_SESSION_LEADER = "insert into eventsessionleader (event, session, leader) values (?, ?, ?)";
	
	private static final String INSERT_LEADER= "insert into leader(name, company, title,companyurl,twitterusername)values(?,?,?,?,?) ";

	private static final String SELECT_EVENT_ID = "SELECT ID FROM EVENT WHERE ID = (SELECT MAX(ID) FROM EVENT)";
	
	private static final String SELECT_MAX_SESSION_ID = "SELECT MAX(ID) FROM EVENTSESSION WHERE EVENT = ?";
	
	private static final String SELECT_VENUE_ID = "SELECT ID FROM VENUE WHERE ID = (SELECT MAX(ID) FROM VENUE)";
	
	private static final String SELECT_EVENTVENUE_ID = "SELECT VENUE FROM EVENTVENUE WHERE EVENT = ?";
		
	private static final String SELECT_ROOM_ID = "SELECT MAX(ID) FROM VENUEROOM WHERE VENUE = ?";
	
	private static final String INSERT_EVENT_VENUE = "insert into eventvenue (event, venue) values (?, ?)";
	
	private static final String SELECT_VENUE = "SELECT NAME FROM VENUE where ID  = ?";
	
	private static final String SELECT_EVENT_TRACKS = "SELECT CODE, NAME, DESCRIPTION FROM EVENTTRACK where EVENT  = ?";
	
	private static final String SELECT_LEADER= "SELECT NAME FROM LEADER where ID  = ?";
	
	private static final String SELECT_LEADER_ID="SELECT MAX(ID) FROM LEADER";
	
	private static final String SELECT_VENUE_ADDRESSES = "SELECT POSTALADDRESS FROM VENUE where ID  = ?";
	
	private static final String SELECT_VENUE_LOCATIONHINTS = "SELECT LOCATIONHINT FROM VENUE where ID  = ?";
	
	private static final String SELECT_ROOM_NAMES = "SELECT NAME FROM VENUEROOM where ID  = ? and VENUE = ?";
		
	private static final String SELECT_NUM_VENUE = "SELECT MAX(ID) FROM VENUE";
	
	private static final String SELECT_NUM_ROOMS = "SELECT MAX(ID) FROM VENUEROOM WHERE VENUE = ?";
	
	private static final String SELECT_NUM_LEADER= "SELECT MAX(ID) FROM LEADER";
	
	private static final String SELECT_UPCOMING_EVENTS = SELECT_EVENT + " where e.endTime > ? order by e.startTime";

	private static final String SELECT_EVENT_BY_SLUG = SELECT_EVENT + " where g.slug = ? and extract(year from e.startTime) = ? and extract(month from e.startTime) = ? and e.slug = ?";

	private static final String SELECT_TRACK_BY_CODE = "select code, name, description from EVENTTRACK where code = ? and event = ?";
	
	private static final String SELECT_FROM_EVENT_SESSION = "select s.id, s.title, s.startTime, s.endTime, s.description, s.hashtag, s.rating, s.venue, s.room, r.name as roomName, (f.attendee is not null) as favorite, l.name from EventSession s ";
	
	private static final String SELECT_TRACK_FORM = "select name, code, description from EVENTTRACK where event = ? and code = ?";
	
	private static final String UPDATE_TRACK_FORM = "update EVENTTRACK set name = ?, code = ?, description = ? where event = ? and code = ?";
	
	private static final String UPDATE_TRACK_ROOMS = "update EVENTSESSION set room = ? where track = ? and event = ?";
	
	private static final String SELECT_EVENT_SESSIONS = SELECT_FROM_EVENT_SESSION +
		"left outer join VenueRoom r on s.venue = r.venue and s.room = r.id " +
		"left outer join EventSessionFavorite f on s.event = f.event and s.id = f.session " +
		"inner join EventSessionLeader sl on s.event = sl.event and s.id = sl.session " +
		"inner join Leader l on sl.leader = l.id " + 
		"where s.event = ?";
	
	private static final String SELECT_SESSION_BY_ID = SELECT_FROM_EVENT_SESSION +
		"left outer join VenueRoom r on s.venue = r.venue and s.room = r.id " +
		"left outer join EventSessionFavorite f on s.event = f.event and s.id = f.session " +
		"inner join EventSessionLeader sl on s.event = sl.event and s.id = sl.session " +
		"inner join Leader l on sl.leader = l.id " + 
		"where s.id = ? and s.event = ?";
	
	private static final String SELECT_SESSIONS_ON_DAY = SELECT_FROM_EVENT_SESSION +
		"left outer join VenueRoom r on s.venue = r.venue and s.room = r.id " +
		"left outer join EventSessionFavorite f on s.event = f.event and s.id = f.session and f.attendee = ? " +
		"inner join EventSessionLeader sl on s.event = sl.event and s.id = sl.session " +
		"inner join Leader l on sl.leader = l.id " + 
		"where s.event = ? and s.startTime >= ? and s.endTime <= ? " +
		"order by s.startTime, s.id, sl.rank";

	private static final String SELECT_EVENT_FAVORITES = SELECT_FROM_EVENT_SESSION +
		"inner join (select top 10 session, count(*) as favoriteCount from EventSessionFavorite group by session) top on s.id = top.session " +
		"left outer join VenueRoom r on s.venue = r.venue and s.room = r.id " +
		"left outer join EventSessionFavorite f on s.event = f.event and s.id = f.session and f.attendee = ? " +
		"inner join EventSessionLeader sl on s.event = sl.event and s.id = sl.session " +
		"inner join Leader l on sl.leader = l.id " + 
		"where s.event = ? " +
		"order by top.favoriteCount desc, s.id, sl.rank";

	private static final String SELECT_ATTENDEE_FAVORITES = SELECT_FROM_EVENT_SESSION +
		"left outer join VenueRoom r on s.venue = r.venue and s.room = r.id " +
		"inner join EventSessionFavorite f on s.event = f.event and s.id = f.session and f.attendee = ? " +	
		"inner join EventSessionLeader sl on s.event = sl.event and s.id = sl.session " +
		"inner join Leader l on sl.leader = l.id " + 
		"where s.event = ? " +
		"order by f.rank, s.id, sl.rank";
}
