package com.springsource.greenhouse.connect;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.encrypt.StringEncryptor;

import com.springsource.greenhouse.account.AccountMapper;

//TODO this is disabled because it's needed in root-context yet scanned by app-servlet-context. not very clean: revisit this.
//@Repository
public class JdbcAccountProviderRepository implements AccountProviderRepository {
	
	private final JdbcTemplate jdbcTemplate;

	private final AccountMapper accountMapper;

	private final StringEncryptor encryptor;
	
	@Autowired
	public JdbcAccountProviderRepository(JdbcTemplate jdbcTemplate, StringEncryptor encryptor, AccountMapper accountMapper) {
		this.jdbcTemplate = jdbcTemplate;
		this.accountMapper = accountMapper;
		this.encryptor = encryptor;
	}

	public AccountProvider findAccountProviderByName(String name) {
		return jdbcTemplate.queryForObject(SELECT_ACCOUNT_PROVIDER_BY_NAME, accountProviderMapper, name);
	}

	private final RowMapper<AccountProvider> accountProviderMapper = new RowMapper<AccountProvider>() {
		public AccountProvider mapRow(ResultSet rs, int row) throws SQLException {
			String name = rs.getString("name");
			if ("facebook".equals(name)) {
				return new JdbcFacebookAccountProvider(name, encryptor.decrypt(rs.getString("apiKey")), encryptor.decrypt(rs.getString("secret")), rs.getString("requestTokenUrl"), rs.getString("authorizeUrl"), rs.getString("accessTokenUrl"), jdbcTemplate, encryptor, accountMapper);
			} else if ("twitter".equals(name)) {
				return new JdbcTwitterAccountProvider(name, encryptor.decrypt(rs.getString("apiKey")), encryptor.decrypt(rs.getString("secret")), rs.getString("requestTokenUrl"), rs.getString("authorizeUrl"), rs.getString("accessTokenUrl"), jdbcTemplate, encryptor, accountMapper);
			} else {
				return new JdbcAccountProvider(name, rs.getString("apiKey"), rs.getString("secret"), rs.getString("requestTokenUrl"), rs.getString("authorizeUrl"), rs.getString("accessTokenUrl"), jdbcTemplate, encryptor, accountMapper);
			}
		}
	};
	
	private static final String SELECT_ACCOUNT_PROVIDER_BY_NAME = "select name, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl from AccountProvider where name = ?";

}