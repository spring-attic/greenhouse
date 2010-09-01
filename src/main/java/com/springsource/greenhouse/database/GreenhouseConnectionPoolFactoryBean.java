package com.springsource.greenhouse.database;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class GreenhouseConnectionPoolFactoryBean implements FactoryBean<DataSource>, InitializingBean, DisposableBean {

	private JdbcConnectionPool connectionPool;
	
	public void afterPropertiesSet() throws Exception {
		connectionPool = JdbcConnectionPool.create("jdbc:h2:~/greenhouse", "sa", "sa");
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
		new GreenhouseDatabaseInstaller(connectionPool).run();
	}
	
}