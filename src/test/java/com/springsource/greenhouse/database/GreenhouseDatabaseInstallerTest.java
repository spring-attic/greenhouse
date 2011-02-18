package com.springsource.greenhouse.database;

import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.springsource.greenhouse.config.database.BaseDatabaseUpgrader;

public class GreenhouseDatabaseInstallerTest {

	@Test
	public void runUpgrader() {
		EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
		factory.setDatabaseType(EmbeddedDatabaseType.H2);
		EmbeddedDatabase db = factory.getDatabase();
		BaseDatabaseUpgrader installer = new BaseDatabaseUpgrader(db);
		installer.run();
		installer.run();
		BaseDatabaseUpgrader installer2 = new BaseDatabaseUpgrader(db);
		installer2.run();
	}
}
