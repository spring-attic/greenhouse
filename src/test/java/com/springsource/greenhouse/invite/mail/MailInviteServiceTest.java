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
package com.springsource.greenhouse.invite.mail;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.database.GreenhouseTestDatabaseBuilder;
import com.springsource.greenhouse.invite.InviteRepository;
import com.springsource.greenhouse.invite.Invitee;
import com.springsource.greenhouse.invite.JdbcInviteRepository;

public class MailInviteServiceTest {
	
	private EmbeddedDatabase db;
	
	private MailInviteService inviteService;
	
	private JdbcTemplate jdbcTemplate;
	
	private MailSender mailSender;
	
	@Before
	public void setUp() {
		db = new GreenhouseTestDatabaseBuilder().member().activity().invite().testData(getClass()).getDatabase();
		jdbcTemplate = new JdbcTemplate(db);		
		InviteRepository inviteRepository = new JdbcInviteRepository(jdbcTemplate, null);
		mailSender = mock(MailSender.class);
		inviteService = new AsyncMailInviteService(mailSender, inviteRepository, "http://localhost:8443/invite/accept?token={token}");
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
		inviteService.sendInvite(account, invitees, invitationText);
		assertEquals(2, jdbcTemplate.queryForInt("select count(*) from Invite"));
		verify(mailSender, times(2)).send(any(SimpleMailMessage.class));
		jdbcTemplate.queryForObject("select token from Invite where email = 'keith.donald@springsource.com'", String.class);
		jdbcTemplate.queryForObject("select token from Invite where email = 'cwalls@vmware.com'", String.class);
	}
	
}
