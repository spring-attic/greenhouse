package com.springsource.greenhouse.connect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.scribe.exceptions.OAuthException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.AccountMapper;
import com.springsource.greenhouse.account.PictureSize;
import com.springsource.greenhouse.account.PictureUrlFactory;
import com.springsource.greenhouse.account.PictureUrlMapper;
import com.springsource.greenhouse.account.StubFileStorage;
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
		AccountMapper accountMapper = new AccountMapper(new PictureUrlMapper(new PictureUrlFactory(new StubFileStorage()), PictureSize.small), new UriTemplate("http://localhost:8080/members/{profileKey}"));				
		providerRepository = new JdbcAccountProviderRepository(jdbcTemplate, accountMapper);
		accountProvider = providerRepository.findAccountProviderByName("test");
	}

	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}

	@Test
	public void getRequestToken() {
		OAuthToken requestToken = accountProvider.getRequestToken(null);
		assertEquals("requestkey", requestToken.getValue());
		assertEquals("requestsecret", requestToken.getSecret());
	}

	@Test(expected=OAuthException.class)
	public void invalidConsumer() {
		AccountProvider badProvider = providerRepository.findAccountProviderByName("bad");
		badProvider.getRequestToken("http://localhost:8080/callback");
	}

	@Test
	public void getAccessToken() {
		OAuthToken requestToken = new OAuthToken("requestkey", "requestsecret");
		OAuthToken accessToken = accountProvider.getAccessToken(requestToken, "doesn't matter, already verified");
		assertEquals("accesskey", accessToken.getValue());
		assertEquals("accesssecret", accessToken.getSecret());
	}

	@Test(expected=OAuthException.class)
	public void invalidRequestToken() {
		OAuthToken requestToken = new OAuthToken("wrongToken", "wrongSecret");
		assertNull(accountProvider.getAccessToken(requestToken, "doesn't matter, already verified"));
	}
}
