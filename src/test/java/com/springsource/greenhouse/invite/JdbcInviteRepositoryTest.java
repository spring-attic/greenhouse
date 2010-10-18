package com.springsource.greenhouse.invite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.transaction.TransactionalMethodRule;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.action.Action;
import com.springsource.greenhouse.action.ActionGateway;
import com.springsource.greenhouse.action.JdbcActionRepository;
import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class JdbcInviteRepositoryTest {
	
	private EmbeddedDatabase db;
	
	private JdbcTemplate jdbcTemplate;

	private JdbcInviteRepository inviteRepository;
	
	@Before
	public void setUp() {
		db = new GreenhouseTestDatabaseBuilder().member().activity().invite().testData(getClass()).getDatabase();
		jdbcTemplate = new JdbcTemplate(db);
		JdbcActionRepository actionRepository = new JdbcActionRepository(jdbcTemplate, new ActionGateway() {
			public void actionPerformed(Action action) {
			}
		});
		inviteRepository = new JdbcInviteRepository(jdbcTemplate, actionRepository);
	}
	
	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}
	
	@Test
	public void getInvite() throws InviteException {
		Invite details = inviteRepository.findInvite("abc");
		assertEquals("Keith", details.getInvitee().getFirstName());
		assertEquals("Donald", details.getInvitee().getLastName());
		assertEquals("keith.donald@springsource.com", details.getInvitee().getEmail());
		assertEquals("rclarkson", details.getSentBy().getId());
		assertEquals("Roy Clarkson", details.getSentBy().getLabel());
	}

	@Test(expected=InviteAlreadyAcceptedException.class)
	public void inviteAlreadyAccepted() throws InviteException {
		inviteRepository.findInvite("def");
	}

	@Test(expected=NoSuchInviteException.class)
	public void noSuchInvite() throws InviteException {
		inviteRepository.findInvite("bogus");
	}

	@Test
	@Transactional
	public void markInviteAccepted() throws InviteException {
		jdbcTemplate.update("insert into Member (firstName, lastName, email, password, username, gender, birthdate) values ('Keith', 'Donald', 'keith.donald@springsource.com', 'kdonald', 'melbourne', 'M', '1977-12-01')");
		Account signedUp = new Account(3L, "Keith", "Donald", "keith.donald@springsource.com", "kdonald", "http://localhost:8080/resources/profile-pics/3.jpg", new UriTemplate("http://localhost:8080/members/{id}"));
		inviteRepository.markInviteAccepted("abc", signedUp);
		try {
			inviteRepository.findInvite("abc");
			fail("Should have failed already accepted");
		} catch (InviteAlreadyAcceptedException e) {
			
		}
	}

	@Rule
	public TransactionalMethodRule transactional = new TransactionalMethodRule();

}