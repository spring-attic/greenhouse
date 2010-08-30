package com.springsource.greenhouse.account;

import javax.inject.Inject;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.Transformer;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.social.account.Account;
import org.springframework.templating.StringTemplate;
import org.springframework.templating.StringTemplateFactory;

public class WelcomeMailTransformer {
	
	private final StringTemplateFactory templateFactory;
	
	private final Resource welcomeTemplate = new ClassPathResource("welcome.st", getClass());

	@Inject
	public WelcomeMailTransformer(StringTemplateFactory templateFactory) {
		this.templateFactory = templateFactory;
	}

	@Transformer
	public MailMessage welcomeMail(Account account) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("noreply@springsource.com");
		mailMessage.setTo(account.getEmail());
		mailMessage.setSubject("Welcome to the Greenhouse!");
		StringTemplate textTemplate;
		textTemplate = templateFactory.getStringTemplate(welcomeTemplate);
		textTemplate.put("firstName", account.getFirstName());
		textTemplate.put("profileKey", account.getProfileKey());
		mailMessage.setText(textTemplate.render());
		return mailMessage;
	}

}