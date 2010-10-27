package com.springsource.greenhouse.database;

import javax.sql.DataSource;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.versioned.DatabaseChangeSetBuilder;

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
