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

import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.versioned.DatabaseChangeSetBuilder;

/**
 * Creates the DataSource connection-pool used by Greenhouse in the standard profile.
 * Will also perform a Database upgrade if necessary.
 * @author Keith Donald
 */
public class ConnectionPoolFactoryBean implements FactoryBean<DataSource>, InitializingBean, DisposableBean {
	
	private JdbcConnectionPool connectionPool;
	
	public void afterPropertiesSet() throws Exception {
		connectionPool = JdbcConnectionPool.create(url, username, password);
		populateDatabase();
	}
	
	public void destroy() throws Exception {
		connectionPool.dispose();
	}

	public DataSource getObject() throws Exception {
		return connectionPool;
	}

	public Class<?> getObjectType() {
		return DataSource.class;
	}

	public boolean isSingleton() {
		return true;
	}
	
	// internal helpers
	
	private void populateDatabase() {
		new BaseDatabaseUpgrader(connectionPool) {
			protected void addInstallChanges(DatabaseChangeSetBuilder builder) {
				builder.addChange(databaseResource("install/data/AccountProviders.sql"));
			}
		}.run();
	}

	@Value("${database.url}")
	private String url;

	@Value("${database.username}")
	private String username;

	@Value("${database.password}")
	private String password;
}