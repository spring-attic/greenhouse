package com.springsource.greenhouse.account;

import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.annotation.Transformer;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.templating.ResourceStringTemplateFactory;
import org.springframework.templating.StringTemplate;
import org.springframework.templating.StringTemplateFactory;

public class WelcomeMailTransformer {
	
	private final StringTemplateFactory welcomeTemplateFactory = new ResourceStringTemplateFactory(new ClassPathResource("welcome.st", getClass()));

	@Transformer
	public MailMessage welcomeMail(Account account) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("noreply@springsource.com");
		mailMessage.setTo(account.getEmail());
		mailMessage.setSubject("Welcome to the Greenhouse!");
		StringTemplate textTemplate;
		textTemplate = welcomeTemplateFactory.getStringTemplate();
		textTemplate.put("firstName", account.getFirstName());
		textTemplate.put("profileUrl", account.getProfileUrl());
		mailMessage.setText(textTemplate.render());
		return mailMessage;
	}

}