package com.springsource.greenhouse.develop.oauth;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth.provider.token.OAuthAccessProviderToken;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.develop.AppConnection;

@SuppressWarnings("serial") 
class AppConnectionProviderToken implements OAuthAccessProviderToken {

	private AppConnection connection;

	private AccountRepository accountRepository;
	
	private Authentication userAuthentication;
	
	public AppConnectionProviderToken(AppConnection connection, AccountRepository accountRepository) {
		this.connection = connection;
		this.accountRepository = accountRepository;
	}

	public String getConsumerKey() {
		return connection.getApiKey();
	}
	
	public String getValue() {
		return connection.getAccessToken();
	}
	
	public String getSecret() {
		return connection.getSecret();
	}
	
	public String getCallbackUrl() {
		return null;
	}

	public String getVerifier() {
		return null;
	}

	public boolean isAccessToken() {
		return true;
	}
	
	public Authentication getUserAuthentication() {
		if (userAuthentication == null) {
			Account account = accountRepository.findById(connection.getAccountId());
			userAuthentication = new UsernamePasswordAuthenticationToken(account, null, (Collection<GrantedAuthority>)null);		
		}
		return userAuthentication;
	}
		
}