package com.springsource.greenhouse.database;

import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.springsource.greenhouse.database.BaseDatabaseInstaller;

public class GreenhouseDatabaseInstallerTest {

	@Test
	public void runUpgrader() {
		EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
		factory.setDatabaseType(EmbeddedDatabaseType.H2);
		EmbeddedDatabase db = factory.getDatabase();
		BaseDatabaseInstaller installer = new BaseDatabaseInstaller(db);
		installer.run();
		installer.run();
		BaseDatabaseInstaller installer2 = new BaseDatabaseInstaller(db);
		installer2.run();
	}
}
