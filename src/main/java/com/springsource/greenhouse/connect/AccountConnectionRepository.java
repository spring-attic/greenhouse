package com.springsource.greenhouse.connect;

import java.util.List;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.ProfileReference;

public interface AccountConnectionRepository {

	void addConnection(Long accountId, String provider, OAuthToken accessToken, String providerAccountId, String providerProfileUrl);

	boolean isConnected(Long accountId, String provider);

	void disconnect(Long accountId, String provider);

	OAuthToken getAccessToken(Long accountId, String provider);

	String getProviderAccountId(Long accountId, String provider);

	Account findAccountByConnection(String provider, String accessToken) throws NoSuchAccountConnectionException;

	List<ProfileReference> findAccountsConnectedTo(String provider, List<String> providerAccountIds);

}