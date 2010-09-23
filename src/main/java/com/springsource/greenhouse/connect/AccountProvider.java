package com.springsource.greenhouse.connect;

import java.util.List;

import org.springframework.web.client.RestOperations;

import com.springsource.greenhouse.account.Account;

public interface AccountProvider {

	String getName();
	
	String getApiKey();

	OAuthToken getRequestToken(String callbackUrl);
	
	String getAuthorizeUrl();
	
	OAuthToken getAccessToken(OAuthToken requestToken, String verifier);
	
	void connect(Long accountId, ConnectionDetails details);
	
	boolean isConnected(Long accountId);

	void updateProviderAccountId(Long accountId, String providerAccountId);

	String getProviderAccountId(Long accountId);

	Account findAccountByConnection(String accessToken) throws NoSuchAccountConnectionException;
	
	List<Account> findAccountsWithProviderAccountIds(List<String> providerAccountIds);

	RestOperations getApi(Long accountId);

	void disconnect(Long accountId);
	
}