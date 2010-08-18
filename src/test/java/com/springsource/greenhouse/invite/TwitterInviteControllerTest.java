package com.springsource.greenhouse.invite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.test.utils.GreenhouseTestDatabaseFactory;

public class TwitterInviteControllerTest {
	
	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;

	private TwitterInviteController controller;

	@Before
	public void setup() {
		db = GreenhouseTestDatabaseFactory.createTestDatabase(new FileSystemResource("src/main/webapp/WEB-INF/database/schema-member.sql"),
				new ClassPathResource("TwitterInviteControllerTest.sql", getClass()));
		jdbcTemplate = new JdbcTemplate(db);
		controller = new TwitterInviteController(null, jdbcTemplate);
	}

	@After
	public void destroy() {
		db.shutdown();
	}

	@Test
	public void friendFinder() {
		Account account = new Account(1L, "Joe", "Schmoe", "joe@schmoe.com");
		Model model = new ExtendedModelMap();
		controller.friendFinder(account, model);
		assertEquals("habuma", model.asMap().get("username"));
	}

	@Test
	public void friendFrinderNotConnected() {
		Account account = new Account(2L, "Sue", "Schmoe", "sue@schmoe.com");
		Model model = new ExtendedModelMap();
		controller.friendFinder(account, model);
		assertFalse(model.containsAttribute("username"));
	}

}
