package com.springsource.greenhouse.config;

import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.social.account.NoOpPasswordEncoder;
import org.springframework.social.account.PasswordEncoder;
import org.springframework.social.account.StandardPasswordEncoder;

import com.jolbox.bonecp.BoneCPDataSource;

public class EnvironmentBeanTest {

	@Test
	public void defaultEnvironment() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com/springsource/greenhouse/config/context.xml");

		PasswordEncoder encoder = context.getBean("passwordEncoder", PasswordEncoder.class);
		assertNotNull(encoder);
		assertTrue(encoder instanceof StandardPasswordEncoder);
		
		DataSource dataSource = context.getBean("dataSource", DataSource.class);
		assertNotNull(dataSource);
		assertTrue(dataSource instanceof BoneCPDataSource);
	}

	@Test
	public void embeddedEnvironment() {
		System.setProperty("environment", "embedded");
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com/springsource/greenhouse/config/context.xml");

		PasswordEncoder encoder = context.getBean("passwordEncoder", PasswordEncoder.class);
		assertNotNull(encoder);
		assertTrue(encoder instanceof NoOpPasswordEncoder);
		
		DataSource dataSource = context.getBean("dataSource", DataSource.class);
		assertNotNull(dataSource);
		assertTrue(dataSource instanceof SimpleDriverDataSource);
	}
	
}
