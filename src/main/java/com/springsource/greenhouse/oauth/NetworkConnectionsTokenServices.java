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

public class NetworkConnectionsTokenServices extends HttpSessionBasedTokenServices {

	static final String INSERT_TOKEN_SQL = "insert into NetworkConnection (userId, network, accessToken, secret) values (?, ?, ?, ?)";
	static final String SELECT_TOKEN_SQL = "select network, accessToken, secret from NetworkConnection where userId=? and network=?";

	private Long userId;
	
	private JdbcTemplate jdbcTemplate;

	public NetworkConnectionsTokenServices(JdbcTemplate jdbcTemplate, HttpSession session, Long userId) {
		super(session);
		this.userId = userId;
		this.jdbcTemplate = jdbcTemplate;
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

	private OAuthConsumerToken getTokenFromDatabase(String resourceId) {
		List<OAuthConsumerToken> accessTokens = jdbcTemplate.query(
				SELECT_TOKEN_SQL, new RowMapper<OAuthConsumerToken>() {
					public OAuthConsumerToken mapRow(ResultSet rs, int rowNum) throws SQLException {
						OAuthConsumerToken token = new OAuthConsumerToken();
						token.setAccessToken(true);
						token.setResourceId(rs.getString("network"));
						token.setValue(rs.getString("accessToken"));
						token.setSecret(rs.getString("secret"));
						return token;
					}
				}, userId, resourceId);
		OAuthConsumerToken accessToken = null;
		if (accessTokens.size() > 0) {
			accessToken = accessTokens.get(0);
		}
		return accessToken;
	}

	private void storeTokenInDB(OAuthConsumerToken token) {
		jdbcTemplate.update(INSERT_TOKEN_SQL, userId, token.getResourceId(),
				token.getValue(), token.getSecret());
	}
}
