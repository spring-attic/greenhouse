package com.springsource.greenhouse.connect;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.security.encrypt.SearchableStringEncryptor;
import org.springframework.security.encrypt.StringEncryptor;

import com.springsource.greenhouse.account.AccountMapper;
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
		StringEncryptor encryptor = new SearchableStringEncryptor("secret", "5b8bd7612cdab5ed");
		AccountMapper accountMapper = new AccountMapper(new StubFileStorage(), "http://localhost:8080/members/{profileKey}");
		providerRepository = new JdbcAccountProviderRepository(jdbcTemplate, encryptor, accountMapper);
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
		assertEquals("123456789", twitterProvider.getApiKey());
		assertEquals("http://www.twitter.com/authorize", twitterProvider.getAuthorizeUrl());

		AccountProvider facebookProvider = providerRepository.findAccountProviderByName("facebook");
		assertEquals("facebook", facebookProvider.getName());
		assertEquals("345678901", facebookProvider.getApiKey());
		assertEquals("http://www.facebook.com/authorize", facebookProvider.getAuthorizeUrl());
	}
	
}
