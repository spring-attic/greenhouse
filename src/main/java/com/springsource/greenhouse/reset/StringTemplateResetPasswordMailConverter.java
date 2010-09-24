package com.springsource.greenhouse.reset;

import javax.inject.Inject;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.templating.StringTemplate;
import org.springframework.templating.StringTemplateFactory;
import org.springframework.web.util.UriTemplate;

public class StringTemplateResetPasswordMailConverter implements Converter<ResetPasswordRequest, SimpleMailMessage> {
	
	private StringTemplateFactory templateFactory;
	
	private Resource resetPasswordTemplate = new ClassPathResource("reset-password.st", getClass());

	private UriTemplate resetUriTemplate;
	
	@Inject
	public StringTemplateResetPasswordMailConverter(StringTemplateFactory templateFactory, String resetUriTemplate) {
		this.templateFactory = templateFactory;
		this.resetUriTemplate = new UriTemplate(resetUriTemplate);
	}
	
	public SimpleMailMessage convert(ResetPasswordRequest request) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("noreply@springsource.com");
		mailMessage.setTo(request.getAccount().getEmail());
		StringTemplate textTemplate;
		mailMessage.setSubject("Reset your Greenhouse password");
		textTemplate = templateFactory.getStringTemplate(resetPasswordTemplate);
		textTemplate.put("firstName", request.getAccount().getFirstName());		
		textTemplate.put("resetUrl", resetUriTemplate.expand(request.getToken()));
		mailMessage.setText(textTemplate.render());
		return mailMessage;
	}
}