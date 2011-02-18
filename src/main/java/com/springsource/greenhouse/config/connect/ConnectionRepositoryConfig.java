package com.springsource.greenhouse.config.connect;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.jdbc.JdbcConnectionRepository;
import org.springframework.social.connect.support.ConnectionRepository;

@Configuration
public class ConnectionRepositoryConfig {

	@Bean
	public ConnectionRepository connectionRepository(DataSource dataSource) {
		return new JdbcConnectionRepository(dataSource, Encryptors.noOpText());
	}
	
}
