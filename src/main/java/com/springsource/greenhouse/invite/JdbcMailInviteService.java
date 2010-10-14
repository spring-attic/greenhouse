package com.springsource.greenhouse.invite;

import java.util.List;

import javax.inject.Inject;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Repository;
import org.springframework.templating.LocalStringTemplate;
import org.springframework.templating.ResourceStringTemplateFactory;
import org.springframework.templating.StringTemplate;
import org.springframework.templating.StringTemplateFactory;

import com.springsource.greenhouse.account.Account;

@Repository
public class JdbcMailInviteService implements MailInviteService {

	private final StringTemplateFactory textTemplateFactory = new ResourceStringTemplateFactory(new ClassPathResource("mail-invite-text.st", getClass()));

	private final MailSender mailSender;

	private final TaskExecutor mailerExecutor;

	@Inject
	public JdbcMailInviteService(MailSender mailSender, TaskExecutor mailerExecutor) {
		this.mailSender = mailSender;
		this.mailerExecutor = mailerExecutor;
	}

	public void sendInvite(Account from, List<Invitee> to, String invite) {
		StringTemplate bodyTemplate = new LocalStringTemplate(invite);
		StringTemplate textTemplate = textTemplateFactory.getStringTemplate();
		textTemplate.put("account", from);
		for (Invitee invitee : to) {
			bodyTemplate.put("invitee", invitee);
			textTemplate.put("body", bodyTemplate.render());
			send(invitee, textTemplate.render());
		}
	}

	private void send(Invitee invitee, String inviteText) {
		final SimpleMailMessage mailMessage = createInviteMailMessage(inviteText, invitee);
		mailerExecutor.execute(new Runnable() {
			public void run() {
				mailSender.send(mailMessage);
			}
		});		
	}
	
	private SimpleMailMessage createInviteMailMessage(String inviteText, Invitee to) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("noreply@springsource.com");
		mailMessage.setTo(to.getEmail());
		mailMessage.setSubject("Your Greenhouse Invitation");
		mailMessage.setText(inviteText);
		return mailMessage;
	}

}
