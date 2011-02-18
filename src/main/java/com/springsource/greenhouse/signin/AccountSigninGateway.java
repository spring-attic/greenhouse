package com.springsource.greenhouse.signin;

import java.io.Serializable;

import javax.inject.Inject;

import org.springframework.social.web.connect.SignInControllerGateway;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.AccountUtils;

public class AccountSigninGateway implements SignInControllerGateway {

	private AccountRepository accountRepository;

	@Inject
	public AccountSigninGateway(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public void signIn(Serializable accountId) {
		Account account = accountRepository.findById(Long.valueOf(String.valueOf(accountId)));
		AccountUtils.signin(account);
	}

}
