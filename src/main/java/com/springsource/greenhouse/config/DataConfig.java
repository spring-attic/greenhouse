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

import javax.inject.Inject;
import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.versioned.DatabaseChangeSet;
import org.springframework.jdbc.versioned.SqlDatabaseChange;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.database.DatabaseUpgrader;

/**
 * Greenhouse RDBMS access configuration.
 * A RDBMS provides the system of record for transactional data in the Greenhouse application.
 * We use {@link JdbcTemplate} to access that data.
 * We use compile-time-woven AspectJ-advice around {@link Transactional} methods to apply transaction management.
 * In "embedded mode", we use an embedded database to ease setup of a developer testing environment.
 * In "standard mode", we connect to a file-based H2 database via a connection pool.
 * @author Keith Donald
 */
@Configuration
@EnableTransactionManagement(mode=AdviceMode.ASPECTJ)
public class DataConfig {

	@Inject
	private DataSource dataSource;
	
	/**
	 * Allows repositories to access RDBMS data using the JDBC API.
	 */
	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}
	
	/**
	 * Allows transactions to be managed against the RDBMS using the JDBC API.
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

	/**
	 * Embedded Data configuration.
	 * @author Keith Donald
	 */
	@Configuration
	@Profile("embedded")
	static class Embedded {

		@Inject
		private Environment environment;

		@Inject
		private TextEncryptor textEncryptor;

		@Bean(destroyMethod="shutdown")
		public DataSource dataSource() {
			EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
			factory.setDatabaseName("greenhouse");
			factory.setDatabaseType(EmbeddedDatabaseType.H2);
			return populateDatabase(factory.getDatabase());		
		}

		// internal helpers
		
		private EmbeddedDatabase populateDatabase(EmbeddedDatabase database) {
			new DatabaseUpgrader(database, environment, textEncryptor) {
				protected void addInstallChanges(DatabaseChangeSet changeSet) {
					changeSet.add(SqlDatabaseChange.inResource(new ClassPathResource("test-data.sql", getClass())));
				}
			}.run();
			return database;
		}

	}
	
	/**
	 * Standard Data configuration.
	 * @author Keith Donald
	 */
	@Configuration
	@Profile("standard")
	static class Standard {

		@Inject
		private Environment environment;

		@Inject
		private TextEncryptor textEncryptor;

		@Bean(destroyMethod="dispose")
		public DataSource dataSource() {
			JdbcConnectionPool dataSource = JdbcConnectionPool.create(environment.getProperty("database.url"),
					environment.getProperty("database.username"), environment.getProperty("database.password"));
			new DatabaseUpgrader(dataSource, environment, textEncryptor).run();
			return dataSource;
		}
		
	}
}
