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
public class JdbcAccountProviderFactory implements AccountProviderFactory {
	
	private final JdbcTemplate jdbcTemplate;

	private final StringEncryptor encryptor;
	
	private final JdbcAccountConnectionRepository connectionRepository;
	
	@Autowired
	public JdbcAccountProviderFactory(JdbcTemplate jdbcTemplate, StringEncryptor encryptor, AccountMapper accountMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.encryptor = encryptor;
		this.connectionRepository = new JdbcAccountConnectionRepository(jdbcTemplate, encryptor, accountMapper);
	}

	public AccountProvider<?> getAccountProvider(String name) {
		return jdbcTemplate.queryForObject(SELECT_ACCOUNT_PROVIDER_BY_NAME, new RowMapper<AccountProvider<?>>() {
			public AccountProvider<?> mapRow(ResultSet rs, int rowNum) throws SQLException {
				AccountProviderParameters parameters = parametersMapper.mapRow(rs, rowNum);
				Class<? extends AccountProvider<?>> implementation = getImplementationClass(rs.getString("implementation"));
				Constructor<? extends AccountProvider<?>> constructor = ClassUtils.getConstructorIfAvailable(implementation, AccountProviderParameters.class, AccountConnectionRepository.class);
				return BeanUtils.instantiateClass(constructor, parameters, connectionRepository);
			}
		}, name);
	}

	@SuppressWarnings("unchecked")
	public <A> AccountProvider<A> getAccountProvider(String name, Class<A> apiType) {
		AccountProvider<?> provider = getAccountProvider(name);
		return (AccountProvider<A>) provider;
	}

	// internal helpers
	
	@SuppressWarnings("unchecked")
	private Class<? extends AccountProvider<?>> getImplementationClass(String implementation) {
		try {
			Class<?> clazz = ClassUtils.forName(implementation, JdbcAccountProviderFactory.class.getClassLoader());
			if (!AccountProvider.class.isAssignableFrom(clazz)) {
				throw new IllegalStateException("Implementation '" + implementation + "' does not implement the AccountProvider interface");
			}
			return (Class<? extends AccountProvider<?>>)clazz; 
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("The AccountProvider implementation was not found in the classpath", e);
		}
	}
	
	private RowMapper<AccountProviderParameters> parametersMapper = new RowMapper<AccountProviderParameters>() {
		public AccountProviderParameters mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new AccountProviderParameters(rs.getString("name"), rs.getString("displayName"), encryptor.decrypt(rs.getString("apiKey")), encryptor.decrypt(rs.getString("secret")),
				rs.getLong("appId"), rs.getString("requestTokenUrl"), rs.getString("authorizeUrl"), rs.getString("accessTokenUrl"));
		}
	};

	private static final String SELECT_ACCOUNT_PROVIDER_BY_NAME = "select name, displayName, implementation, apiKey, secret, appId, requestTokenUrl, authorizeUrl, accessTokenUrl from AccountProvider where name = ?";

}