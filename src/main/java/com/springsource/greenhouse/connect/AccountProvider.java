package com.springsource.greenhouse.connect;

import java.util.Collection;
import java.util.Set;

import org.springframework.web.client.RestOperations;

import com.springsource.greenhouse.account.Account;

public interface AccountProvider {

	/**
	 * The name of the account provider.  Unique.
	 */
	String getName();
	
	/**
	 * The api key that identifies the client application to the provider.
	 */
	String getApiKey();

	Token fetchNewRequestToken(String callbackUrl);
	
	String getAuthorizeUrl();
	
	Token fetchAccessToken(Token requestToken, String verifier);
	
	/**
	 * Connect an account with this provider.
	 */
	void connect(Long accountId, ConnectionDetails details);
	
	/**
	 * True if the account is connected to this provider.
	 */
	boolean isConnected(Long accountId);

	/**
	 * Stores the id of the user on the provider's system with their local account record.
	 */
	void saveProviderAccountId(Long accountId, String providerAccountId);

	/**
	 * The id of the user on the provider's system.
	 */
	String getProviderAccountId(Long accountId);

	Set<Account> findAccountsWithProviderAccountIds(Collection<String> providerAccountIds);

	/**
	 * A generic REST client that can be used to invoke the Provider's API on behalf
	 * of the user with the specified account id.
	 * @param accountId the user's internal account identifier
	 */
	RestOperations getApi(Long accountId);

	/**
	 * Disconnects an account connected to this provider.
	 * If the account is not connected, this method has no effect.
	 */
	void disconnect(Long accountId);
	
}