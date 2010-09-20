package com.springsource.greenhouse.connect;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.FileStorage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.AccountMapper;
import com.springsource.greenhouse.account.PictureSize;
import com.springsource.greenhouse.account.PictureUrlFactory;
import com.springsource.greenhouse.account.PictureUrlMapper;

@Repository
public class JdbcAccountProviderRepository implements AccountProviderRepository {
	
	private final JdbcTemplate jdbcTemplate;

	private final AccountMapper accountMapper;

	@Autowired
	public JdbcAccountProviderRepository(JdbcTemplate jdbcTemplate, FileStorage pictureStorage, String profileUrlTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.accountMapper = new AccountMapper(new PictureUrlMapper(new PictureUrlFactory(pictureStorage),
				PictureSize.small), new UriTemplate(profileUrlTemplate));
	}

	public AccountProvider findAccountProviderByName(String name) {
		return jdbcTemplate.queryForObject(SELECT_ACCOUNT_PROVIDER_BY_NAME, accountProviderMapper, name);
	}

	private final RowMapper<AccountProvider> accountProviderMapper = new RowMapper<AccountProvider>() {
		public AccountProvider mapRow(ResultSet rs, int row) throws SQLException {
			String name = rs.getString("name");
			if ("facebook".equals(name)) {
				return new JdbcFacebookAccountProvider(name, rs.getString("apiKey"), rs.getString("secret"),
						rs.getString("requestTokenUrl"), rs.getString("authorizeUrl"), rs.getString("accessTokenUrl"),
						jdbcTemplate, accountMapper);
			} else if ("twitter".equals(name)) {
				return new JdbcTwitterAccountProvider(name, rs.getString("apiKey"), rs.getString("secret"),
						rs.getString("requestTokenUrl"), rs.getString("authorizeUrl"), rs.getString("accessTokenUrl"),
						jdbcTemplate, accountMapper);
			} else {
				return new JdbcAccountProvider(name, rs.getString("apiKey"), rs.getString("secret"),
						rs.getString("requestTokenUrl"), rs.getString("authorizeUrl"), rs.getString("accessTokenUrl"),
						jdbcTemplate, accountMapper);
			}
		}
	};
	
	private static final String SELECT_ACCOUNT_PROVIDER_BY_NAME = 
		 "select name, apiKey, secret, requestTokenUrl, authorizeUrl, accessTokenUrl from AccountProvider where name = ?";

}