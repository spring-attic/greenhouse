package com.springsource.greenhouse.oauth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth.consumer.token.HttpSessionBasedTokenServices;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;

public class JdbcOAuthConsumerTokenServices extends HttpSessionBasedTokenServices {

    static final String INSERT_TOKEN_SQL = "insert into ConnectedAccount (accessToken, member, accountName, secret) values (?, ?, ?, ?)";
    static final String SELECT_TOKEN_SQL = "select accessToken, accountName, secret from ConnectedAccount where member = ? and accountName = ?";
    static final String DELETE_TOKEN_SQL = "delete from ConnectedAccount where member = ? and accountName = ?";

	private Long memberId;	

	private JdbcTemplate jdbcTemplate;
	
	private HttpSession session;

	public JdbcOAuthConsumerTokenServices(JdbcTemplate jdbcTemplate, HttpSession session, Long memberId) {
		super(session);
		this.memberId = memberId;
		this.jdbcTemplate = jdbcTemplate;
		this.session = session;
	}

	@Override
	public OAuthConsumerToken getToken(String resourceId) throws AuthenticationException {
        OAuthConsumerToken token = super.getToken(resourceId);
		if (token == null) {
			token = getTokenFromDatabase(resourceId);
			if (token != null) {
				super.storeToken(resourceId, token);
			}
		}
		return token;
	}

	@Override
	public void storeToken(String resourceId, OAuthConsumerToken token) {
		// Don't bother storing request tokens in the DB...session-storage is fine
		if (token.isAccessToken()) {
			storeTokenInDB(token);
		}
		super.storeToken(resourceId, token);
	}
	
	public void removeToken(String resourceId) {
	    jdbcTemplate.update(DELETE_TOKEN_SQL, memberId, resourceId);
	    session.removeAttribute(KEY_PREFIX + "#" + resourceId);
	}

	private OAuthConsumerToken getTokenFromDatabase(String resourceId) {
		List<OAuthConsumerToken> accessTokens = jdbcTemplate.query(
				SELECT_TOKEN_SQL, new RowMapper<OAuthConsumerToken>() {
					public OAuthConsumerToken mapRow(ResultSet rs, int rowNum) throws SQLException {
						OAuthConsumerToken token = new OAuthConsumerToken();
						token.setAccessToken(true);
						token.setValue(rs.getString("accessToken"));
						token.setResourceId(rs.getString("accountName"));
						token.setSecret(rs.getString("secret"));
						return token;
					}
				}, memberId, resourceId);
		OAuthConsumerToken accessToken = null;
		if (accessTokens.size() > 0) {
			accessToken = accessTokens.get(0);
		}
		return accessToken;
	}

	private void storeTokenInDB(OAuthConsumerToken token) {
		jdbcTemplate.update(INSERT_TOKEN_SQL, token.getValue(), memberId, token.getResourceId(), token.getSecret());
	}
}
