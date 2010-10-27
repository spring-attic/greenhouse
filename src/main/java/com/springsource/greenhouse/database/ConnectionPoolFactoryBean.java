package com.springsource.greenhouse.database;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.versioned.DatabaseChangeSetBuilder;

public class ConnectionPoolFactoryBean implements FactoryBean<DataSource>, InitializingBean, DisposableBean {

	@Value("${database.url}")
	private String url;

	@Value("${database.username}")
	private String username;

	@Value("${database.password}")
	private String password;
	
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
	
	private void populateDatabase() {
		new BaseDatabaseUpgrader(connectionPool) {
			protected void addInstallChanges(DatabaseChangeSetBuilder builder) {
				builder.addChange(databaseResource("install/data/AccountProviders.sql"));
			}
		}.run();
	}
	
}