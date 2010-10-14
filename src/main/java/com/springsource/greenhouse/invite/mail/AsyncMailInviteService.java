package com.springsource.greenhouse.invite.mail;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.encrypt.SecureRandomStringKeyGenerator;
import org.springframework.security.encrypt.StringKeyGenerator;
import org.springframework.stereotype.Service;
import org.springframework.templating.LocalStringTemplate;
import org.springframework.templating.ResourceStringTemplateFactory;
import org.springframework.templating.StringTemplate;
import org.springframework.templating.StringTemplateFactory;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.invite.InviteRepository;
import com.springsource.greenhouse.invite.Invitee;

@Service
public class AsyncMailInviteService implements MailInviteService {

	private final StringTemplateFactory textTemplateFactory = new ResourceStringTemplateFactory(new ClassPathResource("invite-text.st", getClass()));

	private final MailSender mailSender;

	private final TaskExecutor mailerExecutor;

	private final InviteRepository inviteRepository;

	private final UriTemplate acceptUriTemplate;
	
	@Inject
	public AsyncMailInviteService(MailSender mailSender, TaskExecutor mailerExecutor, InviteRepository inviteRepository,
			@Value("${application.secureUrl}/invite?token={token}") String acceptUriTemplate) {
		this.mailSender = mailSender;
		this.mailerExecutor = mailerExecutor;
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
	
	public void acceptInvite(String token) {
		inviteRepository.removeInvite(token);
	}

	private void send(final Account from, final Invitee to, final String text, final String token) {
		final SimpleMailMessage mailMessage = createInviteMailMessage(to, text);
		mailerExecutor.execute(new Runnable() {
			public void run() {
				mailSender.send(mailMessage);
				inviteRepository.saveInvite(token, to, text, from.getId());
			}
		});
	}
	
	private SimpleMailMessage createInviteMailMessage(Invitee to, String text) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("noreply@springsource.com");
		mailMessage.setTo(to.getEmail());
		mailMessage.setSubject("Your Greenhouse Invitation");
		mailMessage.setText(text);
		return mailMessage;
	}

	private final StringKeyGenerator tokenGenerator = new SecureRandomStringKeyGenerator();
	
}