/*
 * Copyright 2012 the original author or authors.
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
package com.springsource.greenhouse.account;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.signup.WelcomeMailTransformer;

public class WelcomeMailTransformerTest {

	private static final String NEWLINE = System.getProperty("line.separator");

	@Test
	public void welcomeMail() {
		WelcomeMailTransformer transformer = new WelcomeMailTransformer();

		Account account = new Account(1L, "Roy", "Clarkson", "rclarkson@vmware.com", "rclarkson", "http://foo.com/bar.jpg", new UriTemplate("http://greenhouse.springsource.org/members/{id}"));
		SimpleMailMessage welcomeMail = (SimpleMailMessage) transformer.welcomeMail(account);

		assertEquals("rclarkson@vmware.com", welcomeMail.getTo()[0]);
		assertEquals("Greenhouse <noreply@springsource.com>", welcomeMail.getFrom());
		assertEquals("Welcome to the Greenhouse!", welcomeMail.getSubject());
		String mailText = welcomeMail.getText();
		assertTrue(mailText.contains("View your member profile at:" + NEWLINE + "http://greenhouse.springsource.org/members/rclarkson"));
	}

}
