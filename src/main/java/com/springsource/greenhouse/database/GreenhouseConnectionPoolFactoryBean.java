package com.springsource.greenhouse.database;

import javax.sql.DataSource;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import com.jolbox.bonecp.BoneCPDataSource;

public class GreenhouseConnectionPoolFactoryBean implements FactoryBean<DataSource>, InitializingBean, DisposableBean {

	private BoneCPDataSource dataSource;
	
	@Value("${database.url}")
	private String url;
	
	@Value("${database.username}")
	private String username;

	@Value("${database.password}")
	private String password;

	public void afterPropertiesSet() throws Exception {
		dataSource = new BoneCPDataSource();
		dataSource.setJdbcUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
	}
	
	public void destroy() throws Exception {
		dataSource.close();
	}

	public DataSource getObject() throws Exception {
		return dataSource;
	}

	public Class<?> getObjectType() {
		return DataSource.class;
	}

	public boolean isSingleton() {
		return true;
	}
	
}