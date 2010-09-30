package com.springsource.greenhouse.events;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

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
		Event event = eventRepository.findEventBySlug("springone2gx", 2010, 10, "chicago");
		assertNotNull(event);
		assertEquals("SpringOne2gx", event.getTitle());
	}
	
	@Test
	public void findEventSearchString() {
		assertEquals("#s2gx", eventRepository.findEventSearchString(1L));
	}
	
	@Test
	public void findEventSessionSearchString() {
		assertEquals("#s2gx #mvc", eventRepository.findSessionSearchString(1L, (short)1));
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
		assertFalse(eventRepository.toggleFavorite(1L, (short)3, 1L));
		assertTrue(eventRepository.toggleFavorite(1L, (short)3, 1L));
	}
	
	@Test
	public void rate() {
		eventRepository.rate(1L, (short)3, 1L, (short)5, "Rocked");
		eventRepository.rate(1L, (short)3, 2L, (short)4, "Rocked");
		eventRepository.rate(1L, (short)3, 3L, (short)2, "Rocked");
		assertEquals(new Float(3.5), eventRepository.findEventFavorites(1L, 1L).get(0).getRating());
	}

	// internal helpers
	
	private void assertMobile(EventSession session, boolean favorite) {
		assertEquals("Choices in Mobile Application Development", session.getTitle());
		assertEquals(2, session.getLeaders().size());
		Iterator<EventSessionLeader> it = session.getLeaders().iterator();
		EventSessionLeader leader = it.next();
		assertEquals("Roy", leader.getFirstName());
		leader = it.next();
		assertEquals("Keith", leader.getFirstName());
		assertEquals(favorite, session.isFavorite());
	}

	private void assertSocial(EventSession session, boolean favorite) {
		assertEquals("Developing Social-Ready Web Applications", session.getTitle());
		assertEquals(1, session.getLeaders().size());
		assertEquals("Craig", session.getLeaders().iterator().next().getFirstName());
		assertEquals(favorite, session.isFavorite());	
	}
}