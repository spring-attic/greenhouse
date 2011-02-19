package com.springsource.greenhouse.config.database;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public class GreenhouseTestDatabaseBuilder {

	private ResourceDatabasePopulator populator = new ResourceDatabasePopulator();

	public GreenhouseTestDatabaseBuilder member() {
		populator.addScript(new ClassPathResource("install/Member.sql", EmbeddedDataSourceConfig.class));
		return this;
	}

	public GreenhouseTestDatabaseBuilder group() {
		populator.addScript(new ClassPathResource("install/Group.sql", EmbeddedDataSourceConfig.class));
		return this;
	}

	public GreenhouseTestDatabaseBuilder activity() {
		populator.addScript(new ClassPathResource("install/Activity.sql", EmbeddedDataSourceConfig.class));
		return this;
	}

	public GreenhouseTestDatabaseBuilder invite() {
		populator.addScript(new ClassPathResource("install/Invite.sql", EmbeddedDataSourceConfig.class));
		return this;
	}

	public GreenhouseTestDatabaseBuilder venue() {
		populator.addScript(new ClassPathResource("install/Venue.sql", EmbeddedDataSourceConfig.class));
		return this;
	}

	public GreenhouseTestDatabaseBuilder event() {
		populator.addScript(new ClassPathResource("install/Event.sql", EmbeddedDataSourceConfig.class));
		return this;
	}

	public GreenhouseTestDatabaseBuilder connectedAccount() {
		populator.addScript(new ClassPathResource("install/ConnectedAccount.sql", EmbeddedDataSourceConfig.class));
		return this;
	}

	public GreenhouseTestDatabaseBuilder connectedApp() {
		populator.addScript(new ClassPathResource("install/ConnectedApp.sql", EmbeddedDataSourceConfig.class));
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