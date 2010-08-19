package com.springsource.greenhouse.signup;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.AccountUtils;
import com.springsource.greenhouse.account.EmailAlreadyOnFileException;

@Controller
@RequestMapping("/signup")
public class SignupController {

	private final AccountRepository accountRepository;
	
	private final SignedUpGateway gateway;
	
	@Inject
	public SignupController(AccountRepository accountRepository, SignedUpGateway gateway) {
		this.accountRepository = accountRepository;
		this.gateway = gateway;
	}

	@RequestMapping(method = RequestMethod.GET)
	public SignupForm signupForm() {
		return new SignupForm();
	}

	@RequestMapping(method = RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult formBinding) {
		if (formBinding.hasErrors()) {
			return null;
		}
		try {
			Account account = accountRepository.createAccount(form.createPerson());		
			gateway.signedUp(account);
			AccountUtils.signin(account);
		} catch (EmailAlreadyOnFileException e) {
			formBinding.rejectValue("email", "account.duplicateEmail", "already on file");
			return null;
		}
		return "redirect:/";			
	}
}