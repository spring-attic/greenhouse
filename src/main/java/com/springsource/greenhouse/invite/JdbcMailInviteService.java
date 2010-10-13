package com.springsource.greenhouse.invite;

import java.util.List;

import javax.inject.Inject;

import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Repository;
import org.springframework.templating.LocalStringTemplate;
import org.springframework.templating.StringTemplate;

@Repository
public class JdbcMailInviteService implements MailInviteService {

	private MailSender mailSender;

	private TaskExecutor mailerExecutor;

	@Inject
	public JdbcMailInviteService(MailSender mailSender, TaskExecutor mailerExecutor) {
		this.mailSender = mailSender;
		this.mailerExecutor = mailerExecutor;
	}

	public void sendInvite(String invite, List<Invitee> to) {
		StringTemplate inviteTemplate = new LocalStringTemplate(invite);
		for (Invitee invitee : to) {
			inviteTemplate.put("invitee", invitee);
			send(invitee, inviteTemplate.render());
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
