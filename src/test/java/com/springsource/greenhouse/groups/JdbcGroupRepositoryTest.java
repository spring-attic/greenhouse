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
package com.springsource.greenhouse.groups;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class JdbcGroupRepositoryTest {
	
	private EmbeddedDatabase db;

	private JdbcTemplate jdbcTemplate;

	private GroupRepository groupRepository;

	@Before
	public void setup() {
		db = new GreenhouseTestDatabaseBuilder().member().group().testData(getClass()).getDatabase();
		jdbcTemplate = new JdbcTemplate(db);
		groupRepository = new JdbcGroupRepository(jdbcTemplate);
	}
	
	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}
	
	@Test
	public void shouldFindGroupByProfileKey() {
		Group group = groupRepository.findGroupBySlug("test-group");
		assertEquals("Test Group", group.getName());
		assertEquals("This is a test group", group.getDescription());
		assertEquals("#test", group.getHashtag());
	}
	
}
