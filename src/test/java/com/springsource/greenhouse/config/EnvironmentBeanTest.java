package com.springsource.greenhouse.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.social.account.NoOpPasswordEncoder;
import org.springframework.social.account.PasswordEncoder;
import org.springframework.social.account.StandardPasswordEncoder;

public class EnvironmentBeanTest {

	@Test
	public void defaultEnvironment() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com/springsource/greenhouse/config/context.xml");

		PasswordEncoder encoder = context.getBean("passwordEncoder", PasswordEncoder.class);
		assertNotNull(encoder);
		assertTrue(encoder instanceof StandardPasswordEncoder);
	}

	@Test
	public void embeddedEnvironment() {
		System.setProperty("environment", "embedded");
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com/springsource/greenhouse/config/context.xml");

		PasswordEncoder encoder = context.getBean("passwordEncoder", PasswordEncoder.class);
		assertNotNull(encoder);
		assertTrue(encoder instanceof NoOpPasswordEncoder);
	}
	
}
