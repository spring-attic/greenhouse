package com.springsource.greenhouse.account;

import javax.inject.Inject;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.Transformer;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.template.StringTemplate;
import org.springframework.mail.template.StringTemplateFactory;
import org.springframework.stereotype.Component;

@Component
public class WelcomeMailTransformer {
	
	private StringTemplateFactory templateFactory;
	
	private Resource welcomeTemplate = new ClassPathResource("welcome.st", getClass());

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
		textTemplate.put("profileKey", account.getMemberProfileKey());
		mailMessage.setText(textTemplate.render());
		return mailMessage;
	}

}