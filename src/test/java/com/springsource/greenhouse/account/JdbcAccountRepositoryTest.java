package com.springsource.greenhouse.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.security.encrypt.NoOpPasswordEncoder;
import org.springframework.test.transaction.TransactionalMethodRule;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class JdbcAccountRepositoryTest {

	private EmbeddedDatabase db;

	private JdbcAccountRepository accountRepository;

	private JdbcTemplate jdbcTemplate;

	@Before
	public void setup() {
		db = new GreenhouseTestDatabaseBuilder().member().testData(getClass()).getDatabase();
		jdbcTemplate = new JdbcTemplate(db);
		AccountMapper accountMapper = new AccountMapper(new StubFileStorage(), "http://localhost:8080/members/{profileKey}");
		accountRepository = new JdbcAccountRepository(jdbcTemplate, NoOpPasswordEncoder.getInstance(), accountMapper);
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