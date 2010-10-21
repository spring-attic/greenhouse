package com.springsource.greenhouse.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;
import com.springsource.greenhouse.utils.Location;

public class JdbcEventRepositoryTest {

	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;

	private EventRepository eventRepository;

	@Before
	public void setup() {
		db = new GreenhouseTestDatabaseBuilder().member().group().activity().venue().event().testData(getClass()).getDatabase();
		jdbcTemplate = new JdbcTemplate(db);
		eventRepository = new JdbcEventRepository(jdbcTemplate);
	}

	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}

	@Test
	public void findEventBySlug() {
		Event event = eventRepository.findEventBySlug("s2gx", 2010, 10, "chicago");
		assertNotNull(event);
		assertEquals("SpringOne2gx", event.getTitle());
		assertEquals("SpringOne 2GX is a one-of-a-kind conference for application developers, solution architects, web operations and IT teams who develop, deploy and manage business applications.", event.getDescription());
		assertEquals(new DateTime(2010, 10, 19, 17, 0, 0, 0, event.getTimeZone()), event.getStartTime().withZone(event.getTimeZone()));
		assertEquals(new DateTime(2010, 10, 22, 17, 0, 0, 0, event.getTimeZone()), event.getEndTime().withZone(event.getTimeZone()));	
		assertEquals("s2gx", event.getGroup().getId());
		assertEquals("SpringOne2gx", event.getGroup().getLabel());
		assertEquals("Westin Lombard Yorktown Center", event.getVenues().iterator().next().getName());
		assertEquals("70 Yorktown Center Lombard, IL 60148", event.getVenues().iterator().next().getPostalAddress());
		assertEquals(new Location(41.8751108905486, -88.0184300761646), event.getVenues().iterator().next().getLocation());
		assertEquals("adjacent to Shopping Center", event.getVenues().iterator().next().getLocationHint());
	}
	
	@Test
	public void findEventSearchString() {
		assertEquals("#s2gx", eventRepository.findEventSearchString(1L));
	}
	
	@Test
	public void findEventSessionSearchString() {
		assertEquals("#s2gx #mvc", eventRepository.findSessionSearchString(1L, 1));
	}

	@Test
	public void findSessionsOnDay() {
		List<EventSession> sessions = eventRepository.findSessionsOnDay(1L, new LocalDate(2010, 10, 21), 1L);
		assertEquals(2, sessions.size());
		assertSocial(sessions.get(0), true);
		assertMobile(sessions.get(1), true);
	}
	
	@Test
	public void findEventFavorites() {
		List<EventSession> favorites = eventRepository.findEventFavorites(1L, 2L);
		assertEquals(2, favorites.size());
		assertSocial(favorites.get(0), false);
		assertMobile(favorites.get(1), false);
	}

	@Test
	public void findAttendeeFavorites() {
		List<EventSession> favorites = eventRepository.findAttendeeFavorites(1L, 1L);
		assertEquals(2, favorites.size());
		assertSocial(favorites.get(0), true);
		assertMobile(favorites.get(1), true);
	}
	
	@Test
	public void toggleFavorite() {
		assertFalse(eventRepository.toggleFavorite(1L, 3, 1L));
		assertTrue(eventRepository.toggleFavorite(1L, 3, 1L));
	}
	
	@Test
	public void rate() throws SessionNotEndedException {
		eventRepository.rate(2L, 1, 1L, (short)5, "Rocked");
		eventRepository.rate(2L, 1, 2L, (short)4, "Rocked");
		Float rating = eventRepository.rate(2L, 1, 3L, (short)2, "Rocked");
		assertEquals(new Float(3.5), rating);
	}

	// internal helpers
	
	private void assertMobile(EventSession session, boolean favorite) {
		assertEquals("Choices in Mobile Application Development", session.getTitle());
		assertEquals(new DateTime(2010, 10, 21, 19, 45, 0, 0, DateTimeZone.UTC), session.getStartTime());
		assertEquals(new DateTime(2010, 10, 21, 21, 15, 0, 0, DateTimeZone.UTC), session.getEndTime());
		assertEquals(2, session.getLeaders().size());
		assertEquals("Roy", session.getLeaders().get(0).getFirstName());
		assertEquals("Keith", session.getLeaders().get(1).getFirstName());		
		assertEquals(new Float(0), session.getRating());
		assertEquals(favorite, session.isFavorite());
		assertEquals("Junior Ballroom B", session.getRoom().getLabel());
	}

	private void assertSocial(EventSession session, boolean favorite) {
		assertEquals("Developing Social-Ready Web Applications", session.getTitle());
		assertEquals(new DateTime(2010, 10, 21, 17, 45, 0, 0, DateTimeZone.UTC), session.getStartTime());
		assertEquals(new DateTime(2010, 10, 21, 19, 15, 0, 0, DateTimeZone.UTC), session.getEndTime());		
		assertEquals(1, session.getLeaders().size());
		assertEquals("Craig", session.getLeaders().get(0).getFirstName());
		assertEquals(favorite, session.isFavorite());
		assertEquals(new Float(0), session.getRating());
		assertEquals(favorite, session.isFavorite());
		assertEquals("Junior Ballroom B", session.getRoom().getLabel());
	}
}