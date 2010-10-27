package com.springsource.greenhouse.database;

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

public class BaseDatabaseUpgrader {
	
	private DatabaseUpgrader upgrader;

	@Autowired
	public BaseDatabaseUpgrader(DataSource dataSource) {		
		this.upgrader = initUpgrader(dataSource);
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
	
	private DatabaseUpgrader initUpgrader(DataSource dataSource) {
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
		builder.addChange(databaseResource("install/ConnectedAccount.sql"));
		builder.addChange(databaseResource("install/Reset.sql"));
		builder.addChange(databaseResource("install/Invite.sql"));		
		builder.addChange(databaseResource("install/Venue.sql"));
		builder.addChange(databaseResource("install/Event.sql"));
		addInstallChanges(builder);
		upgrader.addChangeSet(builder.getChangeSet());
	}

	private void addUpgradeChangeSets(GenericDatabaseUpgrader upgrader) {
		upgrader.addChangeSet(singletonChangeSet("2", databaseResource("upgrade/474.sql")));
	}
	
}