package com.springsource.greenhouse.connect;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class JdbcAccountProviderITest {
	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;

	private AccountProvider accountProvider;

	private JdbcAccountProviderRepository providerRepository;

	@Before
	public void setup() {
		db = new GreenhouseTestDatabaseBuilder().member().connectedAccount().testData(getClass()).getDatabase();
		jdbcTemplate = new JdbcTemplate(db);
		providerRepository = new JdbcAccountProviderRepository(jdbcTemplate, null,
				"http://greenhouse.springsource.org/members/{profileKey}");
		accountProvider = providerRepository.findAccountProviderByName("test");
	}

	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}

	@Test
	public void fetchNewRequestToken() {
		Token requestToken = accountProvider.fetchNewRequestToken("http://greenhouse.springsource.org");
		assertEquals("requestkey", requestToken.getValue());
		assertEquals("requestsecret", requestToken.getSecret());
	}

	@Test(expected = UnableToFetchRequestTokenException.class)
	public void fetchNewRequestToken_Failed() {
		AccountProvider badProvider = providerRepository.findAccountProviderByName("bad");
		badProvider.fetchNewRequestToken("http://greenhouse.springsource.org");
	}

	@Test
	public void fetchAccessToken() {
		Token requestToken = new Token("requestkey", "requestsecret");
		Token accessToken = accountProvider.fetchAccessToken(requestToken, "huh");
		assertEquals("accesskey", accessToken.getValue());
		assertEquals("accesssecret", accessToken.getSecret());
	}

	@Test
	public void fetchAccessToken_Failed() {
		Token requestToken = new Token("wrongkey", "wrongsecret");
		assertNull(accountProvider.fetchAccessToken(requestToken, "huh"));
	}
}
