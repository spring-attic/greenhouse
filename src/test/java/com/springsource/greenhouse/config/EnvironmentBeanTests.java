package com.springsource.greenhouse.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.jolbox.bonecp.BoneCPDataSource;
import com.springsource.greenhouse.account.NoOpPasswordEncoder;
import com.springsource.greenhouse.account.PasswordEncoder;
import com.springsource.greenhouse.account.StandardPasswordEncoder;

public class EnvironmentBeanTests {

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
