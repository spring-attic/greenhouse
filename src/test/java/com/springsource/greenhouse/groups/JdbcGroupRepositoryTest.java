package com.springsource.greenhouse.groups;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.test.utils.GreenhouseTestDatabaseFactory;

public class JdbcGroupRepositoryTest {
	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;

	private GroupRepository groupRepository;

	@Before
	public void setup() {
		db = GreenhouseTestDatabaseFactory.createTestDatabase(new FileSystemResource("src/main/webapp/WEB-INF/database/schema-member.sql"),
				new FileSystemResource("src/main/webapp/WEB-INF/database/schema-event.sql"),
				new ClassPathResource("JdbcGroupRepositoryTest.sql", getClass()));
		jdbcTemplate = new JdbcTemplate(db);
		groupRepository = new JdbcGroupRepository(jdbcTemplate);
	}
	
	@After
	public void destroy() {
		db.shutdown();
	}
	
	@Test
	public void shouldFindGroupByProfileKey() {
		Group group = groupRepository.findGroupByProfileKey("test");
		assertEquals("Test Group", group.getName());
		assertEquals("This is a test group", group.getDescription());
		assertEquals("#test", group.getHashtag());
	}
	
}
