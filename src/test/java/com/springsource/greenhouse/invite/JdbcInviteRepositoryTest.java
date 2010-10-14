package com.springsource.greenhouse.invite;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class JdbcInviteRepositoryTest {
	
	private EmbeddedDatabase db;
	
	private JdbcTemplate jdbcTemplate;

	private JdbcInviteRepository inviteRepository;
	
	@Before
	public void setUp() {
		db = new GreenhouseTestDatabaseBuilder().member().activity().invite().testData(getClass()).getDatabase();
		jdbcTemplate = new JdbcTemplate(db);		
		inviteRepository = new JdbcInviteRepository(jdbcTemplate);
	}
	
	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}
	
	@Test
	public void getInviteDetails() {
		InviteDetails details = inviteRepository.getInviteDetails("abc");
		assertEquals("Keith", details.getInvitee().getFirstName());
		assertEquals("Donald", details.getInvitee().getLastName());
		assertEquals("keith.donald@springsource.com", details.getInvitee().getEmail());
		assertEquals("rclarkson", details.getSentBy().getId());
		assertEquals("Roy Clarkson", details.getSentBy().getLabel());
	}
	
}
