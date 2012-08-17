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
package com.springsource.greenhouse.develop;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.test.transaction.Transactional;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class JdbcAppRepositoryTest {

	private JdbcAppRepository appRepository;

	private JdbcTemplate jdbcTemplate;

	public JdbcAppRepositoryTest() {
		EmbeddedDatabase db = new GreenhouseTestDatabaseBuilder().member().connectedApp().testData(getClass()).getDatabase();
		transactional = new Transactional(db);
		jdbcTemplate = new JdbcTemplate(db);
		appRepository = new JdbcAppRepository(jdbcTemplate, Encryptors.noOpText());		
	}
	
	@Test
	public void findAppSummaries() {
		List<AppSummary> summaries = appRepository.findAppSummaries(2L);
		assertEquals(1, summaries.size());
		AppSummary summary = summaries.get(0);
		assertEquals("Greenhouse for Facebook", summary.getName());
		assertEquals("Awesome", summary.getDescription());
		assertEquals("http://images.greenhouse.springsource.org/apps/icon-default-app.png", summary.getIconUrl());
		assertEquals("greenhouse-for-facebook", summary.getSlug());

		summaries = appRepository.findAppSummaries(3L);
		assertEquals(2, summaries.size());
		summary = summaries.get(0);
		assertEquals("Greenhouse for the iPhone", summary.getName());
		summary = summaries.get(1);
		assertEquals("Greenhouse for the Android", summary.getName());
	}

	@Test
	public void noAppSummaries() {
		assertEquals(0, appRepository.findAppSummaries(1L).size());
	}

	@Test
	public void findAppBySlug() {
		App app = appRepository.findAppBySlug(3L, "greenhouse-for-the-iphone");
		assertExpectedApp(app);
	}
	
	@Test
	public void findAppByApiKey() throws InvalidApiKeyException {
		App app = appRepository.findAppByApiKey("123456789");
		assertExpectedApp(app);
	}
	
	@Test(expected=InvalidApiKeyException.class)
	public void findByAppKeyInvalidKey() throws InvalidApiKeyException {
		appRepository.findAppByApiKey("invalid");
	}

	@Test
	public void createApp() {
		AppForm form = new AppForm();
		form.setName("My App");
		form.setDescription("My App Description");
		String slug = appRepository.createApp(1L, form);
		assertEquals("my-app", slug);

		App app = appRepository.findAppBySlug(1L, slug);
		assertEquals("My App", app.getSummary().getName());
		assertNotNull(app.getApiKey());
		assertNotNull(app.getSecret());
		assertEquals(null, app.getCallbackUrl());
	}

	@Test
	public void updateApp() {
		AppForm form = appRepository.getAppForm(2L, "greenhouse-for-facebook");
		form.setName("Greenhouse for Twitter");
		form.setWebsite("http://www.twitter.com");
		String slug = appRepository.updateApp(2L, "greenhouse-for-facebook", form);
		assertEquals("greenhouse-for-twitter", slug);
		form = appRepository.getAppForm(2L, "greenhouse-for-twitter");
		assertEquals("Greenhouse for Twitter", form.getName());
		assertEquals("http://www.twitter.com", form.getWebsite());
	}

	@Test
	public void deleteApp() {
		appRepository.deleteApp(2L, "greenhouse-for-facebook");
		assertEquals(0, appRepository.findAppSummaries(2L).size());
	}

	private void assertExpectedApp(App app) {
		assertEquals("Greenhouse for the iPhone", app.getSummary().getName());
		assertEquals("123456789", app.getApiKey());
		assertNotNull("987654321", app.getSecret());
		assertEquals("x-com-springsource-greenhouse://oauth-response", app.getCallbackUrl());
	}

	@Rule
	public Transactional transactional;

}