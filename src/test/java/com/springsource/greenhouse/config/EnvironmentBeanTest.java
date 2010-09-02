package com.springsource.greenhouse.config;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.password.NoOpPasswordEncoder;
import org.springframework.security.password.PasswordEncoder;
import org.springframework.security.password.StandardPasswordEncoder;


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
