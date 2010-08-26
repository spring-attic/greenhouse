package com.springsource.greenhouse.config;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.springsource.greenhouse.account.PasswordEncoder;
import com.springsource.greenhouse.account.StandardPasswordEncoder;

public class EnvironmentBeanTests {
	
	private ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com/springsource/greenhouse/config/context.xml");
	
	@Test
	public void testOk() {
		PasswordEncoder encoder = context.getBean("passwordEncoder", PasswordEncoder.class);
		assertTrue(encoder instanceof StandardPasswordEncoder);
	}
	
}
