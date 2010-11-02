/*
 * Copyright 2010 the original author or authors.
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

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.encrypt.SecureRandomStringKeyGenerator;
import org.springframework.security.encrypt.StringKeyGenerator;
import org.springframework.stereotype.Service;
import org.springframework.templating.LocalStringTemplate;
import org.springframework.templating.ResourceStringTemplateFactory;
import org.springframework.templating.StringTemplate;
import org.springframework.templating.StringTemplateFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.invite.InviteRepository;
import com.springsource.greenhouse.invite.Invitee;

/**
 * @author Keith Donald
 */
@Service
public class AsyncMailInviteService implements MailInviteService {

	private final MailSender mailSender;

	private final InviteRepository inviteRepository;

	private final UriTemplate acceptUriTemplate;
	
	@Inject
	public AsyncMailInviteService(MailSender mailSender, InviteRepository inviteRepository, @Value("${application.secureUrl}/invite/accept?token={token}") String acceptUriTemplate) {
		this.mailSender = mailSender;
		this.inviteRepository = inviteRepository;
		this.acceptUriTemplate = new UriTemplate(acceptUriTemplate);
	}

	public void sendInvite(Account from, List<Invitee> to, String invite) {
		StringTemplate textTemplate = textTemplateFactory.getStringTemplate();
		textTemplate.put("account", from);
		StringTemplate bodyTemplate = new LocalStringTemplate(invite);
		for (Invitee invitee : to) {
			bodyTemplate.put("invitee", invitee);
			String body = bodyTemplate.render();
			textTemplate.put("body", body);
			String token = tokenGenerator.generateKey();
			textTemplate.put("acceptUrl", acceptUriTemplate.expand(token));
			String text = textTemplate.render();
			send(from, invitee, text, token);
		}
	}

	@Async
	@Transactional
	private void send(Account from, Invitee to, String text, String token) {
		SimpleMailMessage mailMessage = createInviteMailMessage(to, text);
		if (!inviteRepository.alreadyInvited(to.getEmail())) {
			inviteRepository.saveInvite(token, to, text, from.getId());
			mailSender.send(mailMessage);
		}
	}
	
	private SimpleMailMessage createInviteMailMessage(Invitee to, String text) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("Greenhouse <noreply@springsource.com>");
		mailMessage.setTo(to.getEmail());
		mailMessage.setSubject("Your Greenhouse Invitation");
		mailMessage.setText(text);
		return mailMessage;
	}

	private final StringTemplateFactory textTemplateFactory = new ResourceStringTemplateFactory(new ClassPathResource("invite-text.st", getClass()));

	private final StringKeyGenerator tokenGenerator = new SecureRandomStringKeyGenerator();
	
}