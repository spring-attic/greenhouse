package com.springsource.greenhouse.events;

import static org.junit.Assert.assertEquals;

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
	public void getEventSearchString() {
		assertEquals("#springone2gx", eventRepository.getEventSearchString(1L));
	}
	
	@Test
	public void getEventSessionSearchString() {
		assertEquals("#springone2gx-201", eventRepository.getEventSessionSearchString(1L, (short)201));
	}

	@Test
	public void findTodaysSessions() {
		assertEquals("#springone2gx-201", eventRepository.getEventSessionSearchString(1L, (short)201));
	}
}