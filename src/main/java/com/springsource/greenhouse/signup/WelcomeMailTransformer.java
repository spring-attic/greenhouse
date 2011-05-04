/*
 * Copyright 2010 the original author or authors.
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
package com.springsource.greenhouse.signup;

import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.annotation.Transformer;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.templating.ResourceStringTemplateFactory;
import org.springframework.templating.StringTemplate;
import org.springframework.templating.StringTemplateFactory;

import com.springsource.greenhouse.account.Account;

/**
 * Transforms a new Account to a Welcome MailMessage.
 * Used as part of the integration-signup.xml pipeline to send a welcome mail to a new member.
 * @author Keith Donald
 */
public class WelcomeMailTransformer {
	
	private final StringTemplateFactory welcomeTemplateFactory = new ResourceStringTemplateFactory(new ClassPathResource("welcome.st", getClass()));

	/**
	 * Perform the Account to MailMessage transformation.
	 */
	@Transformer
	public MailMessage welcomeMail(Account account) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("Greenhouse <noreply@springsource.com>");
		mailMessage.setTo(account.getEmail());
		mailMessage.setSubject("Welcome to the Greenhouse!");
		StringTemplate textTemplate;
		textTemplate = welcomeTemplateFactory.getStringTemplate();
		textTemplate.put("firstName", account.getFirstName());
		textTemplate.put("profileUrl", account.getProfileUrl());
		mailMessage.setText(textTemplate.render());
		return mailMessage;
	}

}