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
	public void connect() {
		assertFalse(accountProvider.isConnected(2L));
		accountProvider.connect(2L, new ConnectionDetails("ACCESS_TOKEN", "Twitter"));
		assertTrue(accountProvider.isConnected(2L));
	}

	@Test
	public void connect_alreadyConnected() {
		assertFalse(accountProvider.isConnected(2L));
		accountProvider.connect(1L, new ConnectionDetails("ACCESS_TOKEN", "Twitter"));
		// TODO: What should be the outcome here?
	}

	@Test
	public void isConnected() {
		assertTrue(accountProvider.isConnected(1L));
	}

	@Test
	public void isConnected_noAccountConnection() {
		assertFalse(accountProvider.isConnected(2L));
	}

	@Test
	public void saveProviderAccountId() {
		assertEquals("habuma", accountProvider.getProviderAccountId(1L));
		accountProvider.saveProviderAccountId(1L, "springdude");
		assertEquals("springdude", accountProvider.getProviderAccountId(1L));
	}

	@Test
	public void getProviderAccountId() {
		assertEquals("habuma", accountProvider.getProviderAccountId(1L));
	}

	@Test
	public void getProviderAccountId_noAccountConnection() {
		assertEquals(null, accountProvider.getProviderAccountId(2L));
	}

	@Test
	public void disconnect() {
		assertTrue(accountProvider.isConnected(1L));
		accountProvider.disconnect(1L);
		assertFalse(accountProvider.isConnected(1L));
	}
}
