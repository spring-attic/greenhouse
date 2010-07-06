package com.springsource.greenhouse.mail;

import javax.inject.Inject;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.Transformer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.template.StringTemplate;
import org.springframework.mail.template.StringTemplateFactory;

import com.springsource.greenhouse.signup.SignupMessage;

public class WelcomeMailTransformer {
	
	private StringTemplateFactory templateFactory;
	
	private Resource welcomeTemplate = new ClassPathResource("welcome-mail.st", getClass());

	@Inject
	public WelcomeMailTransformer(JdbcTemplate jdbcTemplate, StringTemplateFactory templateFactory) {
		this.templateFactory = templateFactory;
	}

	@Transformer
	public MailMessage transform(SignupMessage signupMessage) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("noreply@springsource.com");
		mailMessage.setTo(signupMessage.getEmail());
		mailMessage.setSubject("Welcome to the Greenhouse!");
		StringTemplate textTemplate;
		textTemplate = templateFactory.getStringTemplate(welcomeTemplate);
		textTemplate.put("firstName", signupMessage.getFirstName());
		textTemplate.put("profileKey", signupMessage.getProfileKey());
		mailMessage.setText(textTemplate.render());
		return mailMessage;
	}
}