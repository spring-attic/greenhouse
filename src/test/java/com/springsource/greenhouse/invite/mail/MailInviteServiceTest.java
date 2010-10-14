package com.springsource.greenhouse.invite.mail;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.style.StylerUtils;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.mail.MailSender;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;
import com.springsource.greenhouse.invite.InviteRepository;
import com.springsource.greenhouse.invite.JdbcInviteRepository;

public class MailInviteServiceTest {
	
	private EmbeddedDatabase db;
	
	private MailInviteService inviteService;
	
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void setUp() {
		db = new GreenhouseTestDatabaseBuilder().member().activity().invite().testData(getClass()).getDatabase();
		jdbcTemplate = new JdbcTemplate(db);		
		InviteRepository inviteRepository = new JdbcInviteRepository(jdbcTemplate);
		MailSender mailSender = mock(MailSender.class);
		inviteService = new AsyncMailInviteService(mailSender, new SyncTaskExecutor(), inviteRepository, "http://localhost:8443/invite?token={token}");
	}
	
	@After
	public void destroy() {
		if (db != null) {
			db.shutdown();
		}
	}
	
	@Test
	public void inviteFlow() {
		Account account = new Account(1L, "Roy", "Clarkson", "rclarkson@vmware.com", "rclarkson", "http://localhost:8080/images/rclarkson.jpg", new UriTemplate("http://localhost:8080/members/{id}"));
		List<Invitee> invitees = new ArrayList<Invitee>();
		invitees.add(Invitee.valueOf("Keith Donald <keith.donald@springsource.com>"));
		invitees.add(Invitee.valueOf("Craig Walls <cwalls@vmware.com>"));
		String invitationText = "Come join me at the Greenhouse!";
		inviteService.sendInvite(account, invitees, invitationText);
	
		assertEquals(2, jdbcTemplate.queryForInt("select count(*) from Invite"));
		String token1 = jdbcTemplate.queryForObject("select token from Invite where email = 'keith.donald@springsource.com'", String.class);
		String token2 = jdbcTemplate.queryForObject("select token from Invite where email = 'cwalls@vmware.com'", String.class);

		inviteService.acceptInvite(token1);
		assertEquals(1, jdbcTemplate.queryForInt("select count(*) from Invite"));

		inviteService.acceptInvite(token2);
		assertEquals(0, jdbcTemplate.queryForInt("select count(*) from Invite"));
	}
	
	@Test
	public void convert() throws SecurityException, NoSuchFieldException {
		GenericConversionService service = new GenericConversionService();
		ConversionServiceFactory.addDefaultConverters(service);
		List<Invitee> invitee = (List<Invitee>) service.convert("Keith Donald <keith.donald@springsource.com>,Keri Donald <keridonald@gmail.com>", TypeDescriptor.valueOf(String.class), new TypeDescriptor(getClass().getField("invitees")));
		System.out.println(StylerUtils.style(invitee));
	}
	
	public List<Invitee> invitees;
	
}
