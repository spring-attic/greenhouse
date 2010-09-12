package com.springsource.greenhouse.oauth;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.codec.Base64;
import org.springframework.security.oauth.provider.token.InvalidOAuthTokenException;
import org.springframework.security.oauth.provider.token.OAuthAccessProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderToken;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenImpl;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;

@Transactional
public class JdbcOAuthProviderTokenServices implements OAuthProviderTokenServices {
	
    private Random random = new SecureRandom();

	private JdbcTemplate jdbcTemplate;

	private AccountRepository accountRepository;
	
	@Inject
	public JdbcOAuthProviderTokenServices(JdbcTemplate jdbcTemplate, AccountRepository accountRepository) {
		this.jdbcTemplate = jdbcTemplate;
		this.accountRepository = accountRepository;
	}
	
	public OAuthProviderToken createUnauthorizedRequestToken(String consumerKey, String callbackUrl) throws AuthenticationException {
	    OAuthProviderTokenImpl token = new OAuthProviderTokenImpl();
	    token.setValue(UUID.randomUUID().toString());
	    token.setConsumerKey(consumerKey);
	    token.setSecret(generateSecret());	    
	    token.setCallbackUrl(callbackUrl);
	    token.setTimestamp(System.currentTimeMillis());
	    // TODO save to an in-memory store instead
	    // jdbcTemplate.update("insert into OAuthToken (tokenValue, app, secret, callbackUrl, updateTimestamp) values (?, ?, ?, ?, ?)", token.getValue(), token.getConsumerKey(), token.getSecret(), token.getCallbackUrl(), token.getTimestamp());
		return token;
	}

	public void authorizeRequestToken(String requestToken, String verifier, Authentication authentication) throws AuthenticationException {
		Account account = (Account) authentication.getPrincipal();
		// TODO update in-memory store instead
		// jdbcTemplate.update("update OAuthToken set member = ?, verifier = ?, updateTimestamp = ? where tokenValue = ?", account.getId(), verifier, System.currentTimeMillis(), requestToken);
	}
	
	public OAuthAccessProviderToken createAccessToken(String requestToken) throws AuthenticationException {
		// TODO delete from inmemory store instead
		//Map<String, Object> row = jdbcTemplate.queryForMap("select app, member from OAuthToken where tokenValue = ?", requestToken);	
		//jdbcTemplate.update("delete from OAuthToken where tokenValue = ?", requestToken);
		OAuthProviderTokenImpl token = new OAuthProviderTokenImpl();
	    token.setValue(UUID.randomUUID().toString());
	    token.setAccessToken(true);
	    //token.setConsumerKey(authToken.getConsumerKey());
	    token.setSecret(generateSecret());
	    // todo make compile
	    //Long accountId = ((Account)authToken.getAuthentication().getPrincipal()).getId();
	    //jdbcTemplate.update("delete from ConnectedApp where member = ? and app = ?", token.getConsumerKey(), accountId);	    
	    //jdbcTemplate.update("insert into ConnectedApp (member, app, accessToken, secret) values (?, ?, ?, ?)", appId, row.get("member"), token.getValue(), token.getSecret());
		return token;
	}	
	
	public OAuthProviderToken getToken(final String token) throws AuthenticationException {
		// TODO check inmemory store for oauth token first
		try {
			// todo push this code back into a repository decoupled from spring security oauth
			return jdbcTemplate.queryForObject("select c.member, c.secret a.apiKey, from ConnectedApp c inner join App a on c.app = a.id where accessToken = ?", new RowMapper<OAuthProviderToken>() {
				public OAuthProviderToken mapRow(ResultSet rs, int rowNum) throws SQLException {
					OAuthProviderTokenImpl holder = new OAuthProviderTokenImpl();
					holder.setValue(token);
					holder.setAccessToken(true);
					holder.setConsumerKey(rs.getString("app"));
					holder.setUserAuthentication(createUserAuthentication(rs.getLong("member")));
					holder.setSecret(rs.getString("secret"));
					return holder;
				}
			}, token);
		} catch (EmptyResultDataAccessException e) {
				throw new InvalidOAuthTokenException("Access token '" + token + "' is not valid");
		}
	}
	
	private String generateSecret() {
	    byte[] secretBytes = new byte[80];
	    random.nextBytes(secretBytes);
	    String secret = new String(Base64.encode(secretBytes));
	    return secret;
	}
	
	private Authentication createUserAuthentication(Long memberId) {
		Collection<GrantedAuthority> authorities = Collections.emptySet();
		Account account = accountRepository.findById(memberId);
		return new UsernamePasswordAuthenticationToken(account, "OAuth", authorities);
	}
	
}