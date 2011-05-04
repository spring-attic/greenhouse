/*
 * Copyright 2010-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.config.database;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("standard")
public class StandardDataSourceConfig {

	@Value("#{environment['database.url']}")
	private String url;

	@Value("#{environment['database.username']}")
	private String username;

	@Value("#{environment['database.password']}")
	private String password;
	
	@Bean(destroyMethod="dispose")
	public DataSource dataSource() {
		JdbcConnectionPool dataSource = JdbcConnectionPool.create(url, username, password);
		new BaseDatabaseUpgrader(dataSource).run();
		return dataSource;
	}
	
}
