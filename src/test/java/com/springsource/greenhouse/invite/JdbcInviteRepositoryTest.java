package com.springsource.greenhouse.invite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.test.transaction.Transactional;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.activity.action.Action;
import com.springsource.greenhouse.activity.action.ActionGateway;
import com.springsource.greenhouse.activity.action.JdbcActionRepository;
import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;

public class JdbcInviteRepositoryTest {
	
	private JdbcTemplate jdbcTemplate;

	private JdbcInviteRepository inviteRepository;
	
	public JdbcInviteRepositoryTest() {
		EmbeddedDatabase db = new GreenhouseTestDatabaseBuilder().member().activity().invite().testData(getClass()).getDatabase();
		transactional = new Transactional(db);
		jdbcTemplate = new JdbcTemplate(db);
		JdbcActionRepository actionRepository = new JdbcActionRepository(jdbcTemplate, new ActionGateway() {
			public void actionPerformed(Action action) {
			}
		});
		inviteRepository = new JdbcInviteRepository(jdbcTemplate, actionRepository);
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
	public Transactional transactional;

}