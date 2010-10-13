package com.springsource.greenhouse.reset;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.templating.ResourceStringTemplateFactory;
import org.springframework.templating.StringTemplate;
import org.springframework.web.util.UriTemplate;

@Component
public final class ResetPasswordMailMessageConverter implements Converter<ResetPasswordRequest, SimpleMailMessage> {
	
	private final ResourceStringTemplateFactory resetTemplateFactory = new ResourceStringTemplateFactory(new ClassPathResource("reset-password.st", getClass()));

	private final UriTemplate resetUriTemplate;
	
	@Inject
	public ResetPasswordMailMessageConverter(@Value("${application.secureUrl}/reset?token={token}") String resetUriTemplate) {
		this.resetUriTemplate = new UriTemplate(resetUriTemplate);
	}
	
	public SimpleMailMessage convert(ResetPasswordRequest request) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("noreply@springsource.com");
		mailMessage.setTo(request.getAccount().getEmail());
		StringTemplate textTemplate;
		mailMessage.setSubject("Reset your Greenhouse password");
		textTemplate = resetTemplateFactory.createStringTemplate();
		textTemplate.put("firstName", request.getAccount().getFirstName());		
		textTemplate.put("resetUrl", resetUriTemplate.expand(request.getToken()));
		mailMessage.setText(textTemplate.render());
		return mailMessage;
	}
}