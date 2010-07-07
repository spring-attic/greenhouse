package com.springsource.greenhouse.signin.password;

import javax.inject.Inject;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.Transformer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.template.StringTemplate;
import org.springframework.mail.template.StringTemplateFactory;


public class ResetPasswordMailTransformer {
	private StringTemplateFactory templateFactory;
	
	private Resource welcomeTemplate = new ClassPathResource("password-reset-mail.st", getClass());

	@Inject
	public ResetPasswordMailTransformer(JdbcTemplate jdbcTemplate, StringTemplateFactory templateFactory) {
		this.templateFactory = templateFactory;
	}

	@Transformer
	public MailMessage transform(ResetPasswordMessage resetMessage) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("noreply@greenhouse.springsource.com");
		mailMessage.setTo(resetMessage.getEmail());
		StringTemplate textTemplate;
		mailMessage.setSubject("Reset your Greenhouse password");
		textTemplate = templateFactory.getStringTemplate(welcomeTemplate);
		textTemplate.put("resetMessage", resetMessage);
		mailMessage.setText(textTemplate.render());
		return mailMessage;
	}
}
