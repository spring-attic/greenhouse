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
package com.springsource.greenhouse.events.load;

import static org.junit.Assert.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.client.RequestMatchers.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreators;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class NFJSLoaderTest {

	private static final int SHOW_ID = 271;
	private EmbeddedDatabase db;
	private JdbcTemplate jdbcTemplate;
	private EventLoaderRepository eventLoaderRepository;
	private NFJSLoader loader;

	@Before
	public void setup() {
		db = new GreenhouseTestDatabaseBuilder()
				.member()
				.group()
				.activity()
				.invite()
				.venue()
				.event()
				.testData("com/springsource/greenhouse/events/load/JdbcEventLoaderRepositoryTest.sql").getDatabase();
		jdbcTemplate = new JdbcTemplate(db);
		eventLoaderRepository = new JdbcEventLoaderRepository(jdbcTemplate);
		loader = new NFJSLoader(eventLoaderRepository);		
	}
	
	@After
	public void tearDown() {
		jdbcTemplate.update("drop all objects");		
	}
	
	@Test
	public void loadOnce() {
		setupMockRestServiceServer(loader, 1);
		loader.loadEventData(SHOW_ID);		
		assertRowCounts(1, 1, 85, 36, 112);
		
		assertEventData("SpringOne 2GX", "America/Chicago", "2011-10-25 00:00:00.0", "2011-10-28 23:59:59.0", "S2GX");
		assertVenueData("Chicago Marriott Downtown Magnificent Mile", "540 North Michigan Avenue null Chicago, IL 60611", 41.8920052, -87.6247001, "Chicago, IL", 1L);
		assertLeaderData("Craig Walls", "Craig Walls is the Spring Social Project Lead.", "http://blog.springsource.com/author/cwalls/", "habuma");
		assertEventTimeSlotData(16L, 1L, "DINNER", "2011-10-26 18:30:00.0", "2011-10-26 19:30:00.0");
	}


	@Test
	public void loadOnceThenUpdateWithSameData() {
		setupMockRestServiceServer(loader, 3);
		loader.loadEventData(SHOW_ID);
		assertRowCounts(1, 1, 85, 36, 112);
		loader.loadEventData(SHOW_ID);
		assertRowCounts(1, 1, 85, 36, 112);
		assertEventData("SpringOne 2GX", "America/Chicago", "2011-10-25 00:00:00.0", "2011-10-28 23:59:59.0", "S2GX");
		assertVenueData("Chicago Marriott Downtown Magnificent Mile", "540 North Michigan Avenue null Chicago, IL 60611", 41.8920052, -87.6247001, "Chicago, IL", 1L);
		assertLeaderData("Craig Walls", "Craig Walls is the Spring Social Project Lead.", "http://blog.springsource.com/author/cwalls/", "habuma");
		assertEventTimeSlotData(16L, 1L, "DINNER", "2011-10-26 18:30:00.0", "2011-10-26 19:30:00.0");
	}
	
	@Test
	public void loadOnceThenUpdateNewData() {
		setupMockRestServiceServerWithUpdates(loader);
		loader.loadEventData(SHOW_ID);
		assertRowCounts(1, 1, 85, 36, 112);
		assertEventData("SpringOne 2GX", "America/Chicago", "2011-10-25 00:00:00.0", "2011-10-28 23:59:59.0", "S2GX");
		assertVenueData("Chicago Marriott Downtown Magnificent Mile", "540 North Michigan Avenue null Chicago, IL 60611", 41.8920052, -87.6247001, "Chicago, IL", 1L);
		assertLeaderData("Craig Walls", "Craig Walls is the Spring Social Project Lead.", "http://blog.springsource.com/author/cwalls/", "habuma");
		assertEventTimeSlotData(16L, 1L, "DINNER", "2011-10-26 18:30:00.0", "2011-10-26 19:30:00.0");

		loader.loadEventData(SHOW_ID);
		assertRowCounts(1, 1, 86, 37, 113);
		assertEventData("SpringOne/2GX", "America/Boise", "2012-06-09 00:00:00.0", "2012-06-12 23:59:59.0", "SGX");
		assertVenueData("Pocatello Convention Center", "1234 South Arizona Drive null Pocatello, ID 83201", 41.8920052, -87.6247001, "Pocatello, ID", 1L);
		assertLeaderData("Mr. Craig Walls", "Craig Walls is the Spring Social Project Lead and an avid collector of American Way magazines.", "http://blog.springsource.com/author/craigwalls/", "habumadude");
		assertEventTimeSlotData(16L, 1L, "SUPPER", "2012-06-10 18:30:00.0", "2012-06-10 19:30:00.0");
	}

	private void assertRowCounts(int eventRows, int venueRows, int leaderRows, int timeSlotRows, int sessionRows) {
		assertRowCount("Event", eventRows);
		assertRowCount("ExternalEvent", eventRows);
		assertRowCount("Venue", venueRows);
		assertRowCount("Leader", leaderRows);
		assertRowCount("ExternalLeader", leaderRows);
		assertRowCount("EventTimeSlot", timeSlotRows);
		assertRowCount("ExternalEventTimeSlot", timeSlotRows);
		assertRowCount("EventSession", sessionRows);
		assertRowCount("ExternalEventSession", sessionRows);
	}

	private void assertRowCount(String tableName, int rowCount) {
		assertEquals(tableName, rowCount, jdbcTemplate.queryForInt("select count(*) from " + tableName));
	}

	private void assertEventData(String title, String timeZone, String startTime, String endTime, String slug) {
		Map<String, Object> externalEventData = jdbcTemplate.queryForObject("select event, sourceId, source from ExternalEvent where sourceId=? and source='NFJS'", new ColumnMapRowMapper(), SHOW_ID);
		Long eventId = (Long) externalEventData.get("event");
		assertEquals(Long.valueOf(1), eventId);
		assertEquals(Long.valueOf(SHOW_ID), externalEventData.get("sourceId"));
		assertEquals("NFJS", externalEventData.get("source"));
		
		Map<String, Object> eventData = jdbcTemplate.queryForObject("select id,  title, timeZone, startTime, endTime, slug, description, memberGroup from Event where id=?", new ColumnMapRowMapper(), eventId);
		assertEquals(eventId, eventData.get("id"));
		assertEquals(title, eventData.get("title"));
		assertEquals(timeZone, eventData.get("timeZone"));
		assertEquals(startTime, eventData.get("startTime").toString());
		assertEquals(endTime, eventData.get("endTime").toString());
		assertEquals(slug, eventData.get("slug"));
		assertNull(eventData.get("description"));
		assertEquals(1L, eventData.get("memberGroup"));
	}
	
	private void assertVenueData(String name, String postalAddress, double latitude, double longitude, String locationHint, long createdBy) {
		Map<String, Object> eventVenueData = jdbcTemplate.queryForObject("select event, venue from EventVenue where event=?", new ColumnMapRowMapper(), 1);
		assertEquals(Long.valueOf(1), eventVenueData.get("event"));
		Long venueId = (Long) eventVenueData.get("venue");
		assertEquals(Long.valueOf(1), venueId);
		
		Map<String, Object> venueData = jdbcTemplate.queryForObject("select id, name, postalAddress, latitude, longitude, locationHint, createdBy from Venue where id=?", new ColumnMapRowMapper(), venueId);
		assertEquals(Long.valueOf(1), venueData.get("id"));
		assertEquals(name, venueData.get("name"));
		assertEquals(postalAddress, venueData.get("postalAddress"));
		assertEquals(latitude, venueData.get("latitude"));
		assertEquals(longitude, venueData.get("longitude"));
		assertEquals(locationHint, venueData.get("locationHint"));
		assertEquals(createdBy, venueData.get("createdBy"));
	}
	
	private void assertLeaderData(String name, String bio, String personalUrl, String twitterUsername) {
		Map<String, Object> externalLeaderData = jdbcTemplate.queryForObject("select leader, sourceId, source from ExternalLeader where source='NFJS' and sourceId=?", new ColumnMapRowMapper(), 38);
		Long leaderId = (Long) externalLeaderData.get("leader");
		assertEquals(Long.valueOf(15), leaderId);
		assertEquals(38L, externalLeaderData.get("sourceId"));
		assertEquals("NFJS", externalLeaderData.get("source"));
		
		Map<String, Object> leaderData = jdbcTemplate.queryForObject("select id, name, company, title, location, bio, personalUrl, companyUrl, twitterUsername, member from Leader where id=?", new ColumnMapRowMapper(), leaderId);
		assertEquals(leaderId, leaderData.get("id"));
		assertEquals(name, leaderData.get("name"));
		assertNull(leaderData.get("company"));
		assertNull(leaderData.get("title"));
		assertNull(leaderData.get("location"));
		assertEquals(bio, leaderData.get("bio").toString().trim());
		assertEquals(personalUrl, leaderData.get("personalUrl"));
		assertNull(leaderData.get("companyUrl"));
		assertEquals(twitterUsername, leaderData.get("twitterUsername"));
		assertNull(leaderData.get("member")); // TODO: Might want to figure out how to associate this with GH member table
	}
	
	private void assertEventTimeSlotData(long id, long eventId, String label, String startTime, String endTime) {
		Map<String, Object> externalEventTimeSlotData = jdbcTemplate.queryForObject("select timeSlot, sourceId, source from ExternalEventTimeSlot where timeSlot=?", new ColumnMapRowMapper(), id);
		assertEquals(id, externalEventTimeSlotData.get("timeSlot"));
		assertEquals(6311L, externalEventTimeSlotData.get("sourceId"));
		assertEquals("NFJS", externalEventTimeSlotData.get("source"));
		
		Map<String, Object> eventTimeSlotData = jdbcTemplate.queryForObject("select id, event, label, startTime, endTime from EventTimeSlot where id=?", new ColumnMapRowMapper(), 16);
		assertEquals(id, eventTimeSlotData.get("id"));
		assertEquals(eventId, eventTimeSlotData.get("event"));
		assertEquals(label, eventTimeSlotData.get("label"));
		assertEquals(startTime, eventTimeSlotData.get("startTime").toString());
		assertEquals(endTime, eventTimeSlotData.get("endTime").toString());
	}

	
	private MockRestServiceServer setupMockRestServiceServer(NFJSLoader loader, int numberOfLoads) {
		MockRestServiceServer mockServer = MockRestServiceServer.createServer(loader.getRestTemplate());
		for (int i=0; i < numberOfLoads; i++) {
			mockServer.expect(
					requestTo("https://springone2gx.com/m/data/show_short.json?showId=" + SHOW_ID))
					.andExpect(method(GET))
					.andRespond(ResponseCreators.withSuccess(new ClassPathResource("show_short.json", NFJSLoaderTest.class), APPLICATION_JSON));
			mockServer.expect(
					requestTo("https://springone2gx.com/m/data/show_schedule.json?showId=" + SHOW_ID))
					.andExpect(method(GET))
					.andRespond(ResponseCreators.withSuccess(new ClassPathResource("show_schedule.json", NFJSLoaderTest.class), APPLICATION_JSON));
			mockServer.expect(
					requestTo("https://springone2gx.com/m/data/show_speakers.json?showId=" + SHOW_ID))
					.andExpect(method(GET))
					.andRespond(ResponseCreators.withSuccess(new ClassPathResource("show_speakers.json", NFJSLoaderTest.class), APPLICATION_JSON));
			mockServer.expect(
					requestTo("https://springone2gx.com/m/data/show_topics.json?showId=" + SHOW_ID))
					.andExpect(method(GET))
					.andRespond(ResponseCreators.withSuccess(new ClassPathResource("show_topics.json", NFJSLoaderTest.class), APPLICATION_JSON));
		}
		return mockServer;
	}
	
	private void setupMockRestServiceServerWithUpdates(NFJSLoader loader) {
		MockRestServiceServer mockServer = setupMockRestServiceServer(loader, 1); // setup initial load
		mockServer.expect(
				requestTo("https://springone2gx.com/m/data/show_short.json?showId=" + SHOW_ID))
				.andExpect(method(GET))
				.andRespond(ResponseCreators.withSuccess(new ClassPathResource("show_short_updated.json", NFJSLoaderTest.class), APPLICATION_JSON));
		mockServer.expect(
				requestTo("https://springone2gx.com/m/data/show_schedule.json?showId=" + SHOW_ID))
				.andExpect(method(GET))
				.andRespond(ResponseCreators.withSuccess(new ClassPathResource("show_schedule_updated.json", NFJSLoaderTest.class), APPLICATION_JSON));
		mockServer.expect(
				requestTo("https://springone2gx.com/m/data/show_speakers.json?showId=" + SHOW_ID))
				.andExpect(method(GET))
				.andRespond(ResponseCreators.withSuccess(new ClassPathResource("show_speakers_updated.json", NFJSLoaderTest.class), APPLICATION_JSON));
		mockServer.expect(
				requestTo("https://springone2gx.com/m/data/show_topics.json?showId=" + SHOW_ID))
				.andExpect(method(GET))
				.andRespond(ResponseCreators.withSuccess(new ClassPathResource("show_topics_updated.json", NFJSLoaderTest.class), APPLICATION_JSON));		
	}

}
