package org.springframework.jdbc.versioned;

public interface DatabaseUpgrader {

	public DatabaseVersion getCurrentDatabaseVersion();
	
	public void run();
	
}
