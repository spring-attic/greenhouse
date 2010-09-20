package com.springsource.greenhouse.account;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.security.encrypt.NoOpPasswordEncoder;
import org.springframework.security.encrypt.SearchableStringEncryptor;
import org.springframework.test.transaction.TransactionalMethodRule;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class JdbcAccountRepositoryTest {

	private EmbeddedDatabase db;

	private JdbcAccountRepository accountRepository;

	private JdbcTemplate jdbcTemplate;

	@Before
	public void setup() {
		db = new GreenhouseTestDatabaseBuilder().member().connectedAccount().connectedApp().testData(getClass()).getDatabase();
		jdbcTemplate = new JdbcTemplate(db);
		accountRepository = new JdbcAccountRepository(jdbcTemplate, new SearchableStringEncryptor("secret", "5b8bd7612cdab5ed"),
				NoOpPasswordEncoder.getInstance(), new StubFileStorage(), "http://localhost:8080/members/{profileKey}");
	}

	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}

	@Test
	@Transactional
	public void create() throws EmailAlreadyOnFileException {
		Person person = new Person("Jack", "Black", "jack@black.com", "foobie", Gender.Male, new LocalDate(1977, 12, 1));
		Account account = accountRepository.createAccount(person);
		assertEquals(3L, (long) account.getId());
		assertEquals("Jack Black", account.getFullName());
		assertEquals("jack@black.com", account.getEmail());
		assertEquals("http://localhost:8080/members/3", account.getProfileUrl());
		assertEquals("http://localhost:8080/resources/profile-pics/male/small.jpg", account.getPictureUrl());
	}

	@Test
	public void authenticate() throws UsernameNotFoundException, InvalidPasswordException {
		Account account = accountRepository.authenticate("kdonald", "password");
		assertEquals("Keith Donald", account.getFullName());
	}

	@Test(expected = InvalidPasswordException.class)
	public void authenticateInvalidPassword() throws UsernameNotFoundException, InvalidPasswordException {
		accountRepository.authenticate("kdonald", "bogus");
	}

	@Test
	public void findById() {
		assertExpectedAccount(accountRepository.findById(1L));
	}

	@Test
	public void findtByEmail() throws Exception {
		assertExpectedAccount(accountRepository.findByUsername("cwalls@vmware.com"));
	}

	@Test
	public void findByUsername() throws Exception {
		assertExpectedAccount(accountRepository.findByUsername("habuma"));
	}

	@Test(expected = UsernameNotFoundException.class)
	public void usernameNotFound() throws Exception {
		accountRepository.findByUsername("strangerdanger");
	}

	@Test(expected = UsernameNotFoundException.class)
	public void usernameNotFoundEmail() throws Exception {
		accountRepository.findByUsername("stranger@danger.com");
	}

	@Test
	public void markProfilePictureSet() {
		accountRepository.markProfilePictureSet(1L);
		assertTrue(jdbcTemplate.queryForObject("select pictureSet from Member where id = ?", Boolean.class, 1L));
	}

	// connected account tests

	@Test
	public void connectAccount() throws Exception {
		assertEquals(0, jdbcTemplate.queryForInt("select count(*) from AccountConnection where member = 1 and provider = 'tripit'"));
		accountRepository.connectAccount(1L, "tripit", "accessToken", "cwalls");
		assertEquals(1, jdbcTemplate.queryForInt("select count(*) from AccountConnection where member = 1 and provider = 'tripit'"));
		assertExpectedAccount(accountRepository.findByAccountConnection("tripit", "accessToken"));
	}

	@Test(expected = AccountConnectionAlreadyExists.class)
	public void accountAlreadyConnected() throws Exception {
		accountRepository.connectAccount(1L, "facebook", "accessToken", "cwalls");
	}

	@Test
	public void findConnectedAccount() throws Exception {
		assertExpectedAccount(accountRepository.findByAccountConnection("facebook", "accesstoken"));
	}

	@Test(expected = InvalidAccessTokenException.class)
	public void connectedAccountNotFound() throws Exception {
		accountRepository.findByAccountConnection("badtoken", "facebook");
	}

	@Test
	public void disconnectAccount() {
		assertEquals(1, jdbcTemplate.queryForInt("select count(*) from AccountConnection where member = 1 and provider = 'facebook'"));
		accountRepository.disconnectAccount(1L, "facebook");
		assertEquals(0, jdbcTemplate.queryForInt("select count(*) from AccountConnection where member = 1 and provider = 'facebook'"));
	}

	@Test
	public void findFriendAccounts() throws Exception {
		List<Account> accounts = accountRepository.findFriendAccounts("facebook", Collections.singletonList("1"));
		assertEquals(1, accounts.size());
		assertExpectedAccount(accounts.get(0));
	}

	@Test
	public void connectApp() throws InvalidApiKeyException, InvalidAccessTokenException {
		AppConnection connection = accountRepository.connectApp(1L, "123456789");
		assertEquals((Long) 1L, connection.getAccountId());
		assertEquals("123456789", connection.getApiKey());
		assertNotNull(connection.getAccessToken());
		assertNotNull(connection.getSecret());

		AppConnection connection2 = accountRepository.findAppConnection(connection.getAccessToken());
		assertEquals(connection.getAccountId(), connection2.getAccountId());
		assertEquals(connection.getApiKey(), connection2.getApiKey());
		assertEquals(connection.getAccessToken(), connection2.getAccessToken());
		assertEquals(connection.getSecret(), connection2.getSecret());

	}

	@Test(expected = InvalidApiKeyException.class)
	public void connectAppInvalidApiKey() throws InvalidApiKeyException {
		accountRepository.connectApp(1L, "invalidApiKey");
	}

	@Test
	public void findAppConnection() throws InvalidAccessTokenException {
		AppConnection connection = accountRepository.findAppConnection("234567890");
		assertEquals((Long) 1L, connection.getAccountId());
		assertEquals("123456789", connection.getApiKey());
		assertEquals("234567890", connection.getAccessToken());
		assertEquals("345678901", connection.getSecret());
	}

	@Test
	public void disconnectApp() throws InvalidApiKeyException {
		AppConnection app = accountRepository.connectApp(1L, "123456789");
		accountRepository.disconnectApp(1L, app.getAccessToken());
		try {
			accountRepository.findAppConnection("123456789");
			fail("Should have failed");
		} catch (InvalidAccessTokenException e) {
		}
	}

	private void assertExpectedAccount(Account account) {
		assertEquals("Craig", account.getFirstName());
		assertEquals("Walls", account.getLastName());
		assertEquals("Craig Walls", account.getFullName());
		assertEquals("cwalls@vmware.com", account.getEmail());
		assertEquals("habuma", account.getUsername());
		assertEquals("http://localhost:8080/members/habuma", account.getProfileUrl());
		assertEquals("http://localhost:8080/resources/profile-pics/male/small.jpg", account.getPictureUrl());
	}

	@Rule
	public TransactionalMethodRule transactional = new TransactionalMethodRule();

}