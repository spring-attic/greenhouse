package com.springsource.greenhouse.signup;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.account.AccountRepository;

@Controller
@RequestMapping("/signup")
public class SignupController {

	private final SignupHelper signupHelper;
	
	@Inject
	public SignupController(AccountRepository accountRepository, SignedUpGateway gateway) {
		this.signupHelper = new SignupHelper(accountRepository, gateway);
	}

	@RequestMapping(method=RequestMethod.GET)
	public SignupForm signupForm() {
		return new SignupForm();
	}

	@RequestMapping(method=RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult formBinding) {
		if (formBinding.hasErrors()) {
			return null;
		}
		boolean result = signupHelper.signup(form, formBinding);
		return result ? "redirect:/" : null;
	}
}