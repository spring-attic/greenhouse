package com.springsource.greenhouse.reset;

import javax.inject.Inject;

import org.springframework.core.convert.converter.Converter;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class DefaultResetPasswordMailer implements ResetPasswordMailer {

	private Converter<ResetPasswordRequest, SimpleMailMessage> converter;
	
	private MailSender mailSender;
	
	@Inject
	public DefaultResetPasswordMailer(Converter<ResetPasswordRequest, SimpleMailMessage> converter, MailSender mailSender) {
		this.converter = converter;
		this.mailSender = mailSender;
	}

	@Async
	public void send(ResetPasswordRequest request) {
		SimpleMailMessage mail = converter.convert(request);
		mailSender.send(mail);
	}

}