package com.springsource.greenhouse.connect;

import java.util.List;

import com.springsource.greenhouse.account.Account;

public interface AccountProvider<A> {

	// provider meta-data
	
	String getName();
	
	String getDisplayName();

	String getApiKey();
	
	Long getAppId();

	// connection management
	
	OAuthToken fetchNewRequestToken(String callbackUrl);
	
	String buildAuthorizeUrl(String requestToken);
	
	void connect(Long accountId, OAuthToken requestToken, String verifier);
	
	void addConnection(Long accountId, String accessToken, String providerAccountId);

	boolean isConnected(Long accountId);

	A getApi(Long accountId);

	void disconnect(Long accountId);
	
	// additional finders
	
	String getProviderAccountId(Long accountId);

	String getProviderProfileUrl(A api);

	Account findAccountByConnection(String accessToken) throws NoSuchAccountConnectionException;
	
	List<Account> findAccountsWithProviderAccountIds(List<String> providerAccountIds);

}