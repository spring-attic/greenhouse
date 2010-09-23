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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.security.encrypt.SearchableStringEncryptor;
import org.springframework.security.encrypt.StringEncryptor;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountMapper;
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
		StringEncryptor encryptor = new SearchableStringEncryptor("secret", "5b8bd7612cdab5ed");
		AccountMapper accountMapper = new AccountMapper(new StubFileStorage(), "http://localhost:8080/members/{profileKey}");
		providerRepository = new JdbcAccountProviderRepository(jdbcTemplate, encryptor, accountMapper);
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
	public void connected() {
		assertTrue(accountProvider.isConnected(1L));
	}

	@Test
	public void notConnected() {
		assertFalse(accountProvider.isConnected(2L));
	}

	@Test
	public void updateProviderAccountId() {
		assertEquals("habuma", accountProvider.getProviderAccountId(1L));
		accountProvider.updateProviderAccountId(1L, "springdude");
		assertEquals("springdude", accountProvider.getProviderAccountId(1L));
	}

	@Test
	public void getProviderAccountId() {
		assertEquals("habuma", accountProvider.getProviderAccountId(1L));
	}

	@Test
	public void findConnectedAccount() throws Exception {
		assertNotNull(accountProvider.findAccountByConnection("345678901"));
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
