/*
 * Copyright 2010-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

	@Bean
	public JavaMailSender mailSender(@Value("#{environment['mail.host']?:'localhost'}") String host,
			@Value("#{environment['mail.port']?:25}") int port,
			@Value("#{environment['mail.username']}") String username,
			@Value("#{environment['mail.password']}") String password, 
			@Value("#{environment['mail.smtp.auth']?:false}") boolean mailSmtpAuth,
			@Value("#{environment['mail.smtp.starttls.enable']?:false}") boolean mailSmtpStartTls) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setDefaultEncoding("UTF-8");
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", mailSmtpAuth);
		properties.put("mail.smtp.starttls.enable", mailSmtpStartTls);
		mailSender.setJavaMailProperties(properties);
		return mailSender;
	}
}
