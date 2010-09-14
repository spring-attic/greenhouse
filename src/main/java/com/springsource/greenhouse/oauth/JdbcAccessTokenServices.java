package com.springsource.greenhouse.oauth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.social.oauth.AccessToken;
import org.springframework.social.oauth.AccessTokenServices;

import com.springsource.greenhouse.account.Account;

public class JdbcAccessTokenServices implements AccessTokenServices {

	private JdbcTemplate jdbcTemplate;
	
	public JdbcAccessTokenServices(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public AccessToken getToken(String resourceId, Object principal) throws AuthenticationException {
		assertThatPrincipalIsAnAccount(principal);
		Account account = (Account) principal;
		List<AccessToken> accessTokens = jdbcTemplate.query(SELECT_TOKEN_SQL, new RowMapper<AccessToken>() {
			public AccessToken mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new AccessToken(rs.getString("accessToken"), rs.getString("secret"), rs.getString("provider"));
			}
				}, account.getId(), resourceId);
		AccessToken accessToken = null;
		if (accessTokens.size() > 0) {
			accessToken = accessTokens.get(0);
		}
		return accessToken;
	}

	public void storeToken(String resourceId, Object principal, AccessToken token) {
		assertThatPrincipalIsAnAccount(principal);
		Account account = (Account) principal;
		jdbcTemplate.update(INSERT_TOKEN_SQL, account.getId(), token.getProviderId(), token.getValue(),
					token.getSecret());
	}

	public void removeToken(String resourceId, Object principal) {
		assertThatPrincipalIsAnAccount(principal);
		Account account = (Account) principal;
		jdbcTemplate.update(DELETE_TOKEN_SQL, account.getId(), resourceId);
	}

	private void assertThatPrincipalIsAnAccount(Object principal) {
		if (!(principal instanceof Account)) {
			throw new BadCredentialsException("Expected principal to be an Account object");
		}
	}
	
	static final String SELECT_TOKEN_SQL = "select provider, accessToken, secret from AccountConnection where member = ? and provider = ?";
    static final String INSERT_TOKEN_SQL = "insert into AccountConnection (member, provider, accessToken, secret) values (?, ?, ?, ?)";
    static final String DELETE_TOKEN_SQL = "delete from AccountConnection where member = ? and provider = ?";

}