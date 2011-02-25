package com.springsource.greenhouse.signin;

import java.io.Serializable;

import javax.inject.Inject;

import org.springframework.social.web.signin.SignInService;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.AccountUtils;

public class AccountSignInService implements SignInService {

	private final AccountRepository accountRepository;

	@Inject
	public AccountSignInService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public void signIn(Serializable accountId) {
		Account account = accountRepository.findById((Long) accountId);
		AccountUtils.signin(account);
	}

}
