package com.springsource.greenhouse.account;

public interface AccountRepository {

	Account findAccount(Long id);

	Account findAccount(String username) throws UsernameNotFoundException;

	Account findAccountByConnectedAccountAccessToken(String appName, String accessToken);
}
