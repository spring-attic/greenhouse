package com.springsource.greenhouse.members;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.account.StubFileStorage;
import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class JdbcProfileRepositoryTest {

	private JdbcProfileRepository profileRepository;

	private EmbeddedDatabase db;
	
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setup() {
		db = new GreenhouseTestDatabaseBuilder().member().connectedAccount().testData(getClass()).getDatabase();
		jdbcTemplate = new JdbcTemplate(db);
		profileRepository = new JdbcProfileRepository(jdbcTemplate, new StubFileStorage(), null);
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
		assertExpectedProfile(profileRepository.findByKey("habuma"));
		assertExpectedProfile(profileRepository.findByKey("1"));
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
		assertEquals("Twitter", connectedProfiles.get(1).getName());
	}	

}