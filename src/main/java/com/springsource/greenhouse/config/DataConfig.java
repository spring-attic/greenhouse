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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.config.AdviceMode;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.versioned.DatabaseChangeSetBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.springsource.greenhouse.database.DatabaseUpgrader;

@Configuration
@EnableTransactionManagement(mode=AdviceMode.ASPECTJ)
@Import({Standard.class, Embedded.class})
public class DataConfig {

	@Inject
	private DataSource dataSource;
	
	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

	@Configuration
	@Profile("embedded")
	static class Embedded {

		@Bean(destroyMethod="shutdown")
		public DataSource dataSource() {
			EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
			factory.setDatabaseName("greenhouse");
			factory.setDatabaseType(EmbeddedDatabaseType.H2);
			return populateDatabase(factory.getDatabase());		
		}

		private EmbeddedDatabase populateDatabase(EmbeddedDatabase database) {
			new DatabaseUpgrader(database) {
				protected void addInstallChanges(DatabaseChangeSetBuilder builder) {
					builder.addChange(new ClassPathResource("test-data.sql", getClass()));
				}
			}.run();
			return database;
		}

	}
	
	@Configuration
	@Profile("standard")
	static class Standard {

		@Inject
		private Environment environment;
		
		@Bean(destroyMethod="dispose")
		public DataSource dataSource() {
			JdbcConnectionPool dataSource = JdbcConnectionPool.create(environment.getProperty("database.url"),
					environment.getProperty("database.username"), environment.getProperty("database.password"));
			new DatabaseUpgrader(dataSource).run();
			return dataSource;
		}
		
	}
}
