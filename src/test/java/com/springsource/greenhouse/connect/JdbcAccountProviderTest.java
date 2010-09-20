package com.springsource.greenhouse.connect;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class JdbcAccountProviderTest {
	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;

	private AccountProvider accountProvider;

	@Before
	public void setup() {
		db = new GreenhouseTestDatabaseBuilder().member().connectedAccount().testData(getClass()).getDatabase();
		jdbcTemplate = new JdbcTemplate(db);
		JdbcAccountProviderRepository providerRepository = new JdbcAccountProviderRepository(jdbcTemplate);
		accountProvider = (TwitterAccountProvider) providerRepository.findAccountProviderByName("twitter");
	}

	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}

	@Test
	public void getProviderAccountId() {
		assertEquals("habuma", accountProvider.getProviderAccountId(1L));
	}

	@Test
	public void getProviderAccountId_noAccountConnection() {
		assertEquals(null, accountProvider.getProviderAccountId(2L));
	}
}
