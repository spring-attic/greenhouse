package com.springsource.greenhouse.signup.mail;

import javax.inject.Inject;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.Transformer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.template.StringTemplate;
import org.springframework.mail.template.StringTemplateFactory;


public class SignupMailTransformer {
	private StringTemplateFactory templateFactory;
	
	private Resource welcomeTemplate = new ClassPathResource("welcome-mail.st", getClass());

	@Inject
	public SignupMailTransformer(JdbcTemplate jdbcTemplate, StringTemplateFactory templateFactory) {
		this.templateFactory = templateFactory;
	}

	@Transformer
	public MailMessage transform(SignupMessage signupMessage) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("noreply@greenhouse.springsource.com");
		mailMessage.setTo(signupMessage.getEmail());
		StringTemplate textTemplate;
		mailMessage.setSubject("Welcome to the Greenhouse!");
		textTemplate = templateFactory.getStringTemplate(welcomeTemplate);
		textTemplate.put("signup", signupMessage);
		mailMessage.setText(textTemplate.render());
		return mailMessage;
	}
}
