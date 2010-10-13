package com.springsource.greenhouse.reset;

import javax.inject.Inject;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncResetPasswordMailer implements ResetPasswordMailer {

	private Converter<ResetPasswordRequest, SimpleMailMessage> converter;

	// TODO eliminate this dependency once Spring Aspects supports @Async
	private TaskExecutor mailerExecutor;
	
	private MailSender mailSender;
	
	@Inject
	public AsyncResetPasswordMailer(Converter<ResetPasswordRequest, SimpleMailMessage> converter, MailSender mailSender, TaskExecutor mailerExecutor) {
		this.converter = converter;
		this.mailSender = mailSender;
	}

	@Async
	public void send(final ResetPasswordRequest request) {
		mailerExecutor.execute(new Runnable() {
			public void run() {
				SimpleMailMessage mail = converter.convert(request);
				mailSender.send(mail);
			}
		});
	}

}