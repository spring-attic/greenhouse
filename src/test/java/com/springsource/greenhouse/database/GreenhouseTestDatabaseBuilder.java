/*
 * Copyright 2012 the original author or authors.
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

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public class GreenhouseTestDatabaseBuilder {

	private ResourceDatabasePopulator populator = new ResourceDatabasePopulator();

	public GreenhouseTestDatabaseBuilder member() {
		populator.addScript(new ClassPathResource("install/Member.sql", DatabaseUpgrader.class));
		return this;
	}

	public GreenhouseTestDatabaseBuilder group() {
		populator.addScript(new ClassPathResource("install/Group.sql", DatabaseUpgrader.class));
		return this;
	}

	public GreenhouseTestDatabaseBuilder activity() {
		populator.addScript(new ClassPathResource("install/Activity.sql", DatabaseUpgrader.class));
		return this;
	}

	public GreenhouseTestDatabaseBuilder invite() {
		populator.addScript(new ClassPathResource("install/Invite.sql", DatabaseUpgrader.class));
		return this;
	}

	public GreenhouseTestDatabaseBuilder venue() {
		populator.addScript(new ClassPathResource("install/Venue.sql", DatabaseUpgrader.class));
		return this;
	}

	public GreenhouseTestDatabaseBuilder event() {
		populator.addScript(new ClassPathResource("install/Event.sql", DatabaseUpgrader.class));
		return this;
	}

	public GreenhouseTestDatabaseBuilder connectedAccount() {
		populator.addScript(new ClassPathResource("install/ConnectedAccount.sql", DatabaseUpgrader.class));
		return this;
	}

	public GreenhouseTestDatabaseBuilder connectedApp() {
		populator.addScript(new ClassPathResource("install/ConnectedApp.sql", DatabaseUpgrader.class));
		return this;
	}
	
	public GreenhouseTestDatabaseBuilder testData(Class<?> testClass) {
		populator.addScript(new ClassPathResource(testClass.getSimpleName() + ".sql", testClass));
		return this;
	}
	
	public GreenhouseTestDatabaseBuilder testData(String script) {
		populator.addScript(new ClassPathResource(script));
		return this;
	}

	public GreenhouseTestDatabaseBuilder testData(String script, Class<?> relativeTo) {
		populator.addScript(new ClassPathResource(script, relativeTo));
		return this; 
	}
	
	public EmbeddedDatabase getDatabase() {
		EmbeddedDatabaseFactory databaseFactory = new EmbeddedDatabaseFactory();
		databaseFactory.setDatabaseName("greenhouse");
		databaseFactory.setDatabaseType(EmbeddedDatabaseType.H2);
		databaseFactory.setDatabasePopulator(populator);		
		return databaseFactory.getDatabase();
	}

}