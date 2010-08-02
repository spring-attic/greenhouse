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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.test.utils.GreenhouseTestDatabaseFactory;

public class JdbcEventRepositoryTest {

	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;

	private EventRepository eventRepository;

	@Before
	public void setup() {
		db = GreenhouseTestDatabaseFactory.createTestDatabase(new FileSystemResource("src/main/webapp/WEB-INF/database/schema-member.sql"),
				new FileSystemResource("src/main/webapp/WEB-INF/database/schema-event.sql"),
				new ClassPathResource("JdbcEventRepositoryTest.sql", getClass()));
		jdbcTemplate = new JdbcTemplate(db);
		eventRepository = new JdbcEventRepository(jdbcTemplate);
	}

	@After
	public void destroy() {
		db.shutdown();
	}

	@Test
	public void findUpcomingEvents() {
		List<Event> events = eventRepository.findUpcomingEvents();
		assertEquals(2, events.size());
		assertEquals((Long)2L, events.get(0).getId());
		assertEquals((Long)1L, events.get(1).getId());
	}
	
	@Test
	public void findEventByName() {
		Event event = eventRepository.findEventByName("springone2gx", 2010, 8, "chitown");
		assertNotNull(event);
		assertEquals("Soon_Event", event.getTitle());
	}
	
	@Test
	public void getEventSearchString() {
		assertEquals("#springone2gx", eventRepository.findEventHashtag(1L));
	}
	
	@Test
	public void getEventSessionSearchString() {
		assertEquals("#spr101", eventRepository.findSessionHashtag(2L, (short)1));
	}

	@Test
	public void findTodaysSessions() {
		List<EventSession> todaysSessions = eventRepository.findTodaysSessions(2L, 2L);
		assertEquals(2, todaysSessions.size());
		assertMasteringMvc(todaysSessions.get(0));
		assertSocialWebapps(todaysSessions.get(1));
	}

	@Test
	public void findFavoriteSessions() {
		List<EventSession> todaysSessions = eventRepository.findFavoriteSessions(2L, 2L);
		assertEquals(1, todaysSessions.size());
		assertMasteringMvc(todaysSessions.get(0));
	}
	
	@Test
	public void toggleFavorite() {
		assertFalse(eventRepository.toggleFavorite(2L, (short)1, 2L));
		assertTrue(eventRepository.toggleFavorite(2L, (short)1, 2L));
	}

	// internal helpers
	
	private void assertMasteringMvc(EventSession s1) {
		assertEquals("Mastering MVC 3", s1.getTitle());
		assertEquals(2, s1.getLeaders().size());
		Iterator<EventSessionLeader> it = s1.getLeaders().iterator();
		EventSessionLeader leader = it.next();
		assertEquals("Keith", leader.getFirstName());
		leader = it.next();
		assertEquals("Craig", leader.getFirstName());
		assertEquals(true, s1.isFavorite());
	}

	private void assertSocialWebapps(EventSession s2) {
		assertEquals("Developing Social-Ready Web Applications", s2.getTitle());
		assertEquals(1, s2.getLeaders().size());
		assertEquals("Craig", s2.getLeaders().iterator().next().getFirstName());
		assertEquals(false, s2.isFavorite());	
	}
}