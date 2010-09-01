package com.springsource.greenhouse.groups;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class JdbcGroupRepositoryTest {
	
	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;

	private GroupRepository groupRepository;

	@Before
	public void setup() {
		db = new GreenhouseTestDatabaseBuilder().member().group().testData(getClass()).getDatabase();
		jdbcTemplate = new JdbcTemplate(db);
		groupRepository = new JdbcGroupRepository(jdbcTemplate);
	}
	
	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}
	
	@Test
	public void shouldFindGroupByProfileKey() {
		Group group = groupRepository.findGroupByProfileKey("test");
		assertEquals("Test Group", group.getName());
		assertEquals("This is a test group", group.getDescription());
		assertEquals("#test", group.getHashtag());
	}
	
}
