package com.springsource.greenhouse.database;

import javax.sql.DataSource;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import com.jolbox.bonecp.BoneCPDataSource;

public class GreenhouseConnectionPoolFactoryBean implements FactoryBean<DataSource>, InitializingBean, DisposableBean {

	private BoneCPDataSource connectionPool;
	
	@Value("${database.url}")
	private String url;
	
	@Value("${database.username}")
	private String username;

	@Value("${database.password}")
	private String password;

	public void afterPropertiesSet() throws Exception {
		connectionPool = new BoneCPDataSource();
		connectionPool.setJdbcUrl(url);
		connectionPool.setUsername(username);
		connectionPool.setPassword(password);
		populateDatabase();
	}
	
	public void destroy() throws Exception {
		connectionPool.close();
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