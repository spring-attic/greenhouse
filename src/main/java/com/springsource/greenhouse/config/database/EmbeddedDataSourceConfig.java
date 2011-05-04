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
package com.springsource.greenhouse.config.database;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.versioned.DatabaseChangeSetBuilder;

@Configuration
@Profile("embedded")
public class EmbeddedDataSourceConfig {

	@Bean(destroyMethod="shutdown")
	public DataSource dataSource() {
		EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
		factory.setDatabaseName("greenhouse");
		factory.setDatabaseType(EmbeddedDatabaseType.H2);
		return populateDatabase(factory.getDatabase());		
	}
	
	private EmbeddedDatabase populateDatabase(EmbeddedDatabase database) {
		new BaseDatabaseUpgrader(database) {
			protected void addInstallChanges(DatabaseChangeSetBuilder builder) {
				builder.addChange(databaseResource("test-data.sql"));
			}
		}.run();
		return database;
	}
}
