package com.springsource.greenhouse.connect;

import static java.util.Arrays.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.account.Account;
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
		providerRepository = new JdbcAccountProviderRepository(jdbcTemplate, new StubFileStorage(),
				"http://localhost:8080/members/{profileKey}");
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
	public void findFriendAccounts() throws Exception {
		Set<Account> accounts = accountProvider.findAccountsWithProviderAccountIds(asList("habuma", "rclarkson",
				"nobody"));
		assertEquals(2, accounts.size());
		ArrayList<Account> accountList = new ArrayList<Account>(accounts);
		assertThat(accountList.get(0).getId(), anyOf(is(1L), is(3L)));
		assertThat(accountList.get(1).getId(), anyOf(is(1L), is(3L)));
		assertThat(accountList.get(0).getId(), not(equalTo(accountList.get(1).getId())));
	}

	@Test
	public void disconnect() {
		assertTrue(accountProvider.isConnected(1L));
		accountProvider.disconnect(1L);
		assertFalse(accountProvider.isConnected(1L));
	}
}
