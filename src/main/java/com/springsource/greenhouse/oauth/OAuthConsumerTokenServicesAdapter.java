package com.springsource.greenhouse.oauth;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth.consumer.token.HttpSessionBasedTokenServices;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.social.account.Account;
import org.springframework.social.oauth.AccessTokenServices;

public class OAuthConsumerTokenServicesAdapter extends HttpSessionBasedTokenServices {

    static final String INSERT_TOKEN_SQL = "insert into ConnectedAccount (member, provider, accessToken, secret) values (?, ?, ?, ?)";
    static final String SELECT_TOKEN_SQL = "select provider, accessToken, secret from ConnectedAccount where member = ? and provider = ?";
    static final String DELETE_TOKEN_SQL = "delete from ConnectedAccount where member = ? and provider = ?";

	private Account account;
	
	private HttpSession session;
	
	private AccessTokenServices accessTokenServices;

	public OAuthConsumerTokenServicesAdapter(HttpSession session, AccessTokenServices accessTokenServices,
			Account account) {
		super(session);
		this.account = account;
		this.session = session;
		this.accessTokenServices = accessTokenServices;
	}

	@Override
	public OAuthConsumerToken getToken(String resourceId) throws AuthenticationException {
        OAuthConsumerToken token = super.getToken(resourceId);
		if (token == null) {
			token = accessTokenServices.getToken(resourceId, account);
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
			accessTokenServices.storeToken(resourceId, account, token);
		}
		super.storeToken(resourceId, token);
	}
	
	public void removeToken(String resourceId) {
		accessTokenServices.removeToken(resourceId, account);
	    session.removeAttribute(KEY_PREFIX + "#" + resourceId);
	}
}
