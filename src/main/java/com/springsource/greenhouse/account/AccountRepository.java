package com.springsource.greenhouse.account;

public interface AccountRepository {

	Account findAccount(Long id);

	Account findAccount(String username) throws AccountNotFoundException;

}
