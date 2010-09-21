package com.springsource.greenhouse.account;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.templating.StandardStringTemplateFactory;
import org.springframework.web.util.UriTemplate;

public class WelcomeMailTransformerTest {
	@Test
	public void welcomeMail() {
		WelcomeMailTransformer transformer = new WelcomeMailTransformer(new StandardStringTemplateFactory());

		Account account = new Account(1L, "Roy", "Clarkson", "rclarkson@vmware.com", "rclarkson",
				"http://foo.com/bar.jpg", new UriTemplate("http://greenhouse.springsource.org/members/{id}"));
		SimpleMailMessage welcomeMail = (SimpleMailMessage) transformer.welcomeMail(account);

		assertEquals("rclarkson@vmware.com", welcomeMail.getTo()[0]);
		assertEquals("noreply@springsource.com", welcomeMail.getFrom());
		assertEquals("Welcome to the Greenhouse!", welcomeMail.getSubject());
		String mailText = welcomeMail.getText();
		assertTrue(mailText
				.contains("View your member profile at:\nhttp://greenhouse.springsource.org/members/rclarkson"));
	}
}
