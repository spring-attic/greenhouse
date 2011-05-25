package com.springsource.greenhouse.database;

import org.junit.Test;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;

public class GreenhouseDatabaseInstallerTest {

	@Test
	public void runUpgrader() {
		EmbeddedDatabaseFactory factory = new EmbeddedDatabaseFactory();
		factory.setDatabaseType(EmbeddedDatabaseType.H2);
		EmbeddedDatabase db = factory.getDatabase();
		System.setProperty("security.encryptPassword", "foo");
		System.setProperty("security.encryptSalt", new String(Hex.encode(KeyGenerators.secureRandom().generateKey())));
		DatabaseUpgrader installer = new DatabaseUpgrader(db, new StandardEnvironment(), Encryptors.noOpText());
		installer.run();
		installer.run();
		DatabaseUpgrader installer2 = new DatabaseUpgrader(db, new StandardEnvironment(), Encryptors.noOpText());
		installer2.run();
	}
}
