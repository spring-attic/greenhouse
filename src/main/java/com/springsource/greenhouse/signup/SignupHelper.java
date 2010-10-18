package com.springsource.greenhouse.signup;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.AccountUtils;
import com.springsource.greenhouse.account.EmailAlreadyOnFileException;
import com.springsource.greenhouse.account.Person;

public class SignupHelper {

	private final AccountRepository accountRepository;
	
	private final SignedUpGateway gateway;
	
	public SignupHelper(AccountRepository accountRepository, SignedUpGateway gateway) {
		this.accountRepository = accountRepository;
		this.gateway = gateway;
	}

	public boolean signup(SignupForm form, BindingResult formBinding) {
		return signup(form, formBinding, null);
	}
	
	public boolean signup(SignupForm form, BindingResult formBinding, SignupCallback callback) {
		try {
			Account account = createAccount(form.createPerson(),callback);
			gateway.signedUp(account);
			AccountUtils.signin(account);
			return true;
		} catch (EmailAlreadyOnFileException e) {
			formBinding.rejectValue("email", "account.duplicateEmail", "already on file");
			return false;
		}		
	}
	
	@Transactional
	private Account createAccount(Person person, SignupCallback callback) throws EmailAlreadyOnFileException {
		Account account = accountRepository.createAccount(person);
		if (callback != null) {
			callback.postCreateAccount(account);
		}
		return account;
	}
	
	public interface SignupCallback {
		void postCreateAccount(Account account);
	}

}