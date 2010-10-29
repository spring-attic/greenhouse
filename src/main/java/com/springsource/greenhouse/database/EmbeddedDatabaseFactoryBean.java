/*
 * Copyright 2010 the original author or authors.
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
package com.springsource.greenhouse.database;

import javax.sql.DataSource;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.versioned.DatabaseChangeSetBuilder;

/**
 * Creates the DataSource used by Greenhouse in the embedded profile.
 * Will also load a bunch of test data so the application can be developer tested and demoed.
 * @author Keith Donald
 */
public class EmbeddedDatabaseFactoryBean implements FactoryBean<DataSource>, InitializingBean, DisposableBean {

	private EmbeddedDatabase database;
	
	public void afterPropertiesSet() throws Exception {
		EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
		factory.setDatabaseName("greenhouse");
		factory.setDatabaseType(EmbeddedDatabaseType.H2);
		database = factory.getDatabase();
		populateDatabase();
	}
	
	public void destroy() throws Exception {
		database.shutdown();
	}

	public DataSource getObject() throws Exception {
		return database;
	}

	public Class<?> getObjectType() {
		return DataSource.class;
	}

	public boolean isSingleton() {
		return true;
	}

	private void populateDatabase() {
		new BaseDatabaseUpgrader(database) {
			protected void addInstallChanges(DatabaseChangeSetBuilder builder) {
				builder.addChange(databaseResource("install/embedded/test-data.sql"));
			}
		}.run();
	}
}
