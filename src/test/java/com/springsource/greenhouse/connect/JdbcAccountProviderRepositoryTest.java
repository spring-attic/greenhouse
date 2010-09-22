package com.springsource.greenhouse.connect;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.account.StubFileStorage;
import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class JdbcAccountProviderRepositoryTest {
	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;

	private JdbcAccountProviderRepository providerRepository;

	@Before
	public void setup() {
		db = new GreenhouseTestDatabaseBuilder().member().connectedAccount().testData(getClass()).getDatabase();
		jdbcTemplate = new JdbcTemplate(db);
		providerRepository = new JdbcAccountProviderRepository(jdbcTemplate, new StubFileStorage(),
				"http://localhost:8080/members/{profileKey}");
	}

	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}

	@Test
	public void findAccountProviderByName() {
		AccountProvider twitterProvider = providerRepository.findAccountProviderByName("twitter");
		assertEquals("twitter", twitterProvider.getName());
		assertEquals("whatev", twitterProvider.getApiKey());
		assertEquals("http://www.twitter.com/authorize", twitterProvider.getAuthorizeUrl());

		AccountProvider facebookProvider = providerRepository.findAccountProviderByName("facebook");
		assertEquals("facebook", facebookProvider.getName());
		assertEquals("apiKey", facebookProvider.getApiKey());
		assertEquals("http://www.facebook.com/authorize", facebookProvider.getAuthorizeUrl());
	}
}
