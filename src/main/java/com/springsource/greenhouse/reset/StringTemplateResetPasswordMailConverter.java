package com.springsource.greenhouse.reset;

import javax.inject.Inject;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.template.StringTemplate;
import org.springframework.mail.template.StringTemplateFactory;
import org.springframework.stereotype.Component;

@Component
public class StringTemplateResetPasswordMailConverter implements Converter<ResetPasswordRequest, SimpleMailMessage> {
	
	private StringTemplateFactory templateFactory;
	
	private Resource resetPasswordTemplate = new ClassPathResource("reset-password.st", getClass());

	@Inject
	public StringTemplateResetPasswordMailConverter(StringTemplateFactory templateFactory) {
		this.templateFactory = templateFactory;
	}
	
	public SimpleMailMessage convert(ResetPasswordRequest request) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("noreply@greenhouse.springsource.com");
		mailMessage.setTo(request.getAccount().getEmail());
		StringTemplate textTemplate;
		mailMessage.setSubject("Reset your Greenhouse password");
		textTemplate = templateFactory.getStringTemplate(resetPasswordTemplate);
		textTemplate.put("firstName", request.getAccount().getFirstName());		
		textTemplate.put("token", request.getToken());
		mailMessage.setText(textTemplate.render());
		return mailMessage;
	}
}