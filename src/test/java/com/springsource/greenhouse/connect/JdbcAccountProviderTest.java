package com.springsource.greenhouse.connect;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountMapper;
import com.springsource.greenhouse.account.PictureSize;
import com.springsource.greenhouse.account.PictureUrlFactory;
import com.springsource.greenhouse.account.PictureUrlMapper;
import com.springsource.greenhouse.account.StubFileStorage;
import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class JdbcAccountProviderTest {
	
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
		accountProvider = providerRepository.findAccountProviderByName("twitter");
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
		accountProvider.connect(2L, new ConnectionDetails("ACCESS_TOKEN", "SECRET", "Twitter"));
		assertTrue(accountProvider.isConnected(2L));
	}

	@Test
	public void connect_alreadyConnected() {
		assertFalse(accountProvider.isConnected(2L));
		accountProvider.connect(1L, new ConnectionDetails("ACCESS_TOKEN", "SECRET", "Twitter"));
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
		accountProvider.updateProviderAccountId(1L, "springdude");
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
	public void findConnectedAccount() throws Exception {
		assertNotNull(accountProvider.findAccountByConnection("accesstoken"));
	}

	@Test(expected = NoSuchAccountConnectionException.class)
	public void connectedAccountNotFound() throws Exception {
		accountProvider.findAccountByConnection("badtoken");
	}
	
	@Test
	public void findFriendAccounts() throws Exception {
		List<Account> accounts = accountProvider.findAccountsWithProviderAccountIds(asList("habuma", "rclarkson", "BarakObama"));
		assertEquals(2, accounts.size());
	}

	@Test
	public void disconnect() {
		assertTrue(accountProvider.isConnected(1L));
		accountProvider.disconnect(1L);
		assertFalse(accountProvider.isConnected(1L));
	}
	
}
