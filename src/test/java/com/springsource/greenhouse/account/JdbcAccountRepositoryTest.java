/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.account;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.test.transaction.Transactional;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class JdbcAccountRepositoryTest {

	private JdbcAccountRepository accountRepository;

	private JdbcTemplate jdbcTemplate;

	public JdbcAccountRepositoryTest() {
		EmbeddedDatabase db = new GreenhouseTestDatabaseBuilder().member().testData(getClass()).getDatabase();
		transactional = new Transactional(db);
		jdbcTemplate = new JdbcTemplate(db);
		AccountMapper accountMapper = new AccountMapper(new StubFileStorage(), "http://localhost:8080/members/{profileKey}");
		accountRepository = new JdbcAccountRepository(jdbcTemplate, NoOpPasswordEncoder.getInstance(), accountMapper);
	}

	@Test
	public void create() throws EmailAlreadyOnFileException {
		Person person = new Person("Jack", "Black", "jack@black.com", "foobie", Gender.MALE, new LocalDate(1977, 12, 1));
		Account account = accountRepository.createAccount(person);
		assertEquals(3L, (long) account.getId());
		assertEquals("Jack Black", account.getFullName());
		assertEquals("jack@black.com", account.getEmail());
		assertEquals("http://localhost:8080/members/3", account.getProfileUrl());
		assertEquals("http://localhost:8080/resources/profile-pics/male/small.jpg", account.getPictureUrl());
	}

	@Test
	public void authenticate() throws SignInNotFoundException, InvalidPasswordException {
		Account account = accountRepository.authenticate("kdonald", "password");
		assertEquals("Keith Donald", account.getFullName());
	}

	@Test(expected = InvalidPasswordException.class)
	public void authenticateInvalidPassword() throws SignInNotFoundException, InvalidPasswordException {
		accountRepository.authenticate("kdonald", "bogus");
	}

	@Test
	public void findById() {
		assertExpectedAccount(accountRepository.findById(1L));
	}

	@Test
	public void findProfileReferencesByIds() {
		List<ProfileReference> references = accountRepository.findProfileReferencesByIds(Arrays.asList(1L, 2L));
		assertEquals(2, references.size());
	}

	@Test
	public void findtBySigninEmail() throws Exception {
		assertExpectedAccount(accountRepository.findBySignin("cwalls@vmware.com"));
	}

	@Test
	public void findBySigninUsername() throws Exception {
		assertExpectedAccount(accountRepository.findBySignin("habuma"));
	}

	@Test(expected = SignInNotFoundException.class)
	public void usernameNotFound() throws Exception {
		accountRepository.findBySignin("strangerdanger");
	}

	@Test(expected = SignInNotFoundException.class)
	public void usernameNotFoundEmail() throws Exception {
		accountRepository.findBySignin("stranger@danger.com");
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
	public Transactional transactional;
}