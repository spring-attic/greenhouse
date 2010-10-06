package com.springsource.greenhouse.members;

import java.util.List;

import org.springframework.social.core.SocialProviderOperations;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.connect.AccountProvider;
import com.springsource.greenhouse.connect.NoSuchAccountConnectionException;
import com.springsource.greenhouse.connect.OAuthToken;

public class StubAccountProvider implements AccountProvider<SocialProviderOperations> {
	
	private String name;

	public StubAccountProvider(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return name;
	}

	public String getApiKey() {
		return "APIKEY_for_" + name;
	}

	public Long getAppId() {
		return 99L;
	}

	public OAuthToken fetchNewRequestToken(String callbackUrl) {
		return null;
	}

	public String buildAuthorizeUrl(String requestToken) {
		return null;
	}

	public void connect(Long accountId, OAuthToken requestToken, String verifier) {
		
	}

	public void addConnection(Long accountId, String accessToken, String providerAccountId) {
		
	}

	public boolean isConnected(Long accountId) {
		return false;
	}

	public void updateProviderAccountId(Long accountId, String providerAccountId) {
	}

	public String getProviderAccountId(Long accountId) {
		return null;
	}

	public Account findAccountByConnection(String accessToken) throws NoSuchAccountConnectionException {
		return null;
	}

	public List<Account> findAccountsWithProviderAccountIds(List<String> providerAccountIds) {
		return null;
	}

	public SocialProviderOperations getApi(Long accountId) {
		return new SocialProviderOperations() {

			public void setStatus(String arg0) {
			}

			public String getProfileUrl() {
				return "http://" + name + ".com/profile/accountId";
			}

			public String getProfileId() {
				return null;
			}
		};
	}

	public void disconnect(Long accountId) {
	}
}
