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
package com.springsource.greenhouse.members;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.account.StubFileStorage;
import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

@Ignore
public class JdbcProfileRepositoryTest {

	private JdbcProfileRepository profileRepository;

	private EmbeddedDatabase db;
	
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setup() {
		db = new GreenhouseTestDatabaseBuilder().member().connectedAccount().testData(getClass()).getDatabase();
		jdbcTemplate = new JdbcTemplate(db);
		profileRepository = new JdbcProfileRepository(jdbcTemplate, new StubFileStorage());
    }
	
	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}

	@Test
	public void findByAccountId() {
		assertExpectedProfile(profileRepository.findByAccountId(1L));
	}

	@Test
	public void findByKey() {
		assertExpectedProfile(profileRepository.findById("habuma"));
		assertExpectedProfile(profileRepository.findById("1"));
	}

	@Test
	public void findConnectedProfiles() {
		assertExpectedConnectedProfiles(profileRepository.findConnectedProfiles(1L));
	}

	@Test
	public void noConnectedProfiles() {
		List<ConnectedProfile> connectedProfiles = profileRepository.findConnectedProfiles(2L);
		assertEquals(0, connectedProfiles.size());
	}

	// helpers
	
	private void assertExpectedProfile(Profile profile) {
	    assertEquals("Craig Walls", profile.getDisplayName());
    }

	private void assertExpectedConnectedProfiles(List<ConnectedProfile> connectedProfiles) {
	    assertEquals(2, connectedProfiles.size());
		assertEquals("Facebook", connectedProfiles.get(0).getName());
		assertEquals("http://www.facebook.com/profile.php?id=123456789", connectedProfiles.get(0).getUrl());
		assertEquals("Twitter", connectedProfiles.get(1).getName());
		assertEquals("http://www.twitter.com/habuma", connectedProfiles.get(1).getUrl());
	}	

}