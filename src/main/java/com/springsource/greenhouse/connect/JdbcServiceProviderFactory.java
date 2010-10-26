package com.springsource.greenhouse.connect;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.encrypt.StringEncryptor;
import org.springframework.stereotype.Repository;
import org.springframework.util.ClassUtils;

import com.springsource.greenhouse.account.AccountMapper;

@Repository
public class JdbcServiceProviderFactory implements ServiceProviderFactory {
	
	private final JdbcTemplate jdbcTemplate;

	private final StringEncryptor encryptor;
	
	private final JdbcAccountConnectionRepository connectionRepository;
	
	@Autowired
	public JdbcServiceProviderFactory(JdbcTemplate jdbcTemplate, StringEncryptor encryptor, AccountMapper accountMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.encryptor = encryptor;
		this.connectionRepository = new JdbcAccountConnectionRepository(jdbcTemplate, encryptor, accountMapper);
	}

	public ServiceProvider<?> getServiceProvider(String name) {
		return jdbcTemplate.queryForObject(SELECT_SERVICE_PROVIDER_BY_NAME, new RowMapper<ServiceProvider<?>>() {
			public ServiceProvider<?> mapRow(ResultSet rs, int rowNum) throws SQLException {
				ServiceProviderParameters parameters = parametersMapper.mapRow(rs, rowNum);
				Class<? extends ServiceProvider<?>> implementation = getImplementationClass(rs.getString("implementation"));
				Constructor<? extends ServiceProvider<?>> constructor = ClassUtils.getConstructorIfAvailable(implementation, ServiceProviderParameters.class, AccountConnectionRepository.class);
				return BeanUtils.instantiateClass(constructor, parameters, connectionRepository);
			}
		}, name);
	}

	@SuppressWarnings("unchecked")
	public <A> ServiceProvider<A> getServiceProvider(String name, Class<A> apiType) {
		ServiceProvider<?> provider = getServiceProvider(name);
		return (ServiceProvider<A>) provider;
	}

	// internal helpers
	
	@SuppressWarnings("unchecked")
	private Class<? extends ServiceProvider<?>> getImplementationClass(String implementation) {
		try {
			Class<?> clazz = ClassUtils.forName(implementation, JdbcServiceProviderFactory.class.getClassLoader());
			if (!ServiceProvider.class.isAssignableFrom(clazz)) {
				throw new IllegalStateException("Implementation '" + implementation + "' does not implement the ServiceProvider interface");
			}
			return (Class<? extends ServiceProvider<?>>)clazz; 
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("The ServiceProvider implementation was not found in the classpath", e);
		}
	}
	
	private RowMapper<ServiceProviderParameters> parametersMapper = new RowMapper<ServiceProviderParameters>() {
		public ServiceProviderParameters mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new ServiceProviderParameters(rs.getString("name"), rs.getString("displayName"), encryptor.decrypt(rs.getString("apiKey")), encryptor.decrypt(rs.getString("secret")),
				rs.getLong("appId"), rs.getString("requestTokenUrl"), rs.getString("authorizeUrl"), rs.getString("accessTokenUrl"));
		}
	};

	private static final String SELECT_SERVICE_PROVIDER_BY_NAME = "select name, displayName, implementation, apiKey, secret, appId, requestTokenUrl, authorizeUrl, accessTokenUrl from ServiceProvider where name = ?";

}