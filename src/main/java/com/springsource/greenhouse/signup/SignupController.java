package com.springsource.greenhouse.signup;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.utils.SecurityUtils;

@Controller
@RequestMapping("/signup")
public class SignupController {

	private final SignupService signupService;
	
	@Inject
	public SignupController(SignupService signupService) {
		this.signupService = signupService;
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
			Account account = signupService.signup(form.createPerson());			
			SecurityUtils.signin(account);
		} catch (EmailAlreadyOnFileException e) {
			formBinding.rejectValue("email", "account.duplicateEmail", "already on file");
			return null;
		}
		return "redirect:/";			
	}
}