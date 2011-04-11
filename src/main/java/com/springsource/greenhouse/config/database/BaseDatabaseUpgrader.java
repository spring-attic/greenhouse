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

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.versioned.DatabaseChangeSet;
import org.springframework.jdbc.versioned.DatabaseChangeSetBuilder;
import org.springframework.jdbc.versioned.DatabaseUpgrader;
import org.springframework.jdbc.versioned.DatabaseVersion;
import org.springframework.jdbc.versioned.GenericDatabaseUpgrader;
import org.springframework.social.connect.jdbc.JdbcMultiUserServiceProviderConnectionRepository;

/**
 * Performs migrations against the Greenhouse database.
 * Allows a database migration to be automated as part of integrating a software change that impacts the schema.
 * If the Database is at version zero(), the current version of the database will be installed.
 * If the Database is at a version less than the current version, the database will be upgraded from that version to the current version.
 * If the Database is already at the current version, no migration will be performed.
 * This migration model was adapted from the <a href="http://www.liquibase.org/tutorial-using-oracle">LiquiBase Oracle tutorial</a>.
 * @author Keith Donald
 */
class BaseDatabaseUpgrader {
	
	private final DatabaseUpgrader upgrader;

	@Autowired
	public BaseDatabaseUpgrader(DataSource dataSource) {		
		this.upgrader = createUpgrader(dataSource);
	}
	
	@PostConstruct
	public void run() {
		upgrader.run();		
	}

	// subclassing hooks
	
	protected void addInstallChanges(DatabaseChangeSetBuilder builder) {}

	protected Resource databaseResource(String resource) {
		return new ClassPathResource(resource, getClass());
	}

	protected DatabaseChangeSet singletonChangeSet(String version, Resource resource) {
		return new DatabaseChangeSetBuilder(DatabaseVersion.valueOf(version)).addChange(resource).getChangeSet();
	}
	
	// internal helpers
	
	private DatabaseUpgrader createUpgrader(DataSource dataSource) {
		GenericDatabaseUpgrader upgrader = new GenericDatabaseUpgrader(dataSource);
		if (upgrader.getCurrentDatabaseVersion().equals(DatabaseVersion.zero())) {
			addInstallChangeSet(upgrader);			
		} else {
			addUpgradeChangeSets(upgrader);
		}
		return upgrader;
	}

	private void addInstallChangeSet(GenericDatabaseUpgrader upgrader) {
		DatabaseChangeSetBuilder builder = new DatabaseChangeSetBuilder(DatabaseVersion.valueOf("2"));
		builder.addChange(databaseResource("install/Member.sql"));
		builder.addChange(databaseResource("install/Group.sql"));
		builder.addChange(databaseResource("install/Activity.sql"));
		builder.addChange(databaseResource("install/ConnectedApp.sql"));
		builder.addChange(databaseResource("install/Reset.sql"));
		builder.addChange(databaseResource("install/Invite.sql"));		
		builder.addChange(databaseResource("install/Venue.sql"));
		builder.addChange(databaseResource("install/Event.sql"));
		builder.addChange(new ClassPathResource("JdbcServiceProviderConnectionRepositorySchema.sql", JdbcMultiUserServiceProviderConnectionRepository.class));
		addInstallChanges(builder);
		upgrader.addChangeSet(builder.getChangeSet());
	}

	private void addUpgradeChangeSets(GenericDatabaseUpgrader upgrader) {
		upgrader.addChangeSet(singletonChangeSet("2", databaseResource("upgrade/475.sql")));
	}
	
}