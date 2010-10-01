package com.springsource.greenhouse.connect;

import java.util.List;

import com.springsource.greenhouse.account.Account;

public interface AccountProvider<A> {

	String getName();
	
	String getDisplayName();

	String getApiKey();
	
	Long getAppId();

	OAuthToken getRequestToken();
	
	String getAuthorizeUrl(String requestToken);
	
	A connect(Long accountId, OAuthToken requestToken, String verifier);
	
	A addConnection(Long accountId, String accessToken, String providerAccountId);

	boolean isConnected(Long accountId);

	void updateProviderAccountId(Long accountId, String providerAccountId);

	String getProviderAccountId(Long accountId);

	Account findAccountByConnection(String accessToken) throws NoSuchAccountConnectionException;
	
	List<Account> findAccountsWithProviderAccountIds(List<String> providerAccountIds);

	A getApi(Long accountId);

	void disconnect(Long accountId);
	
}