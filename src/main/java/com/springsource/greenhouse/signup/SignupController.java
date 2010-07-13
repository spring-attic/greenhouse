package com.springsource.greenhouse.signup;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.security.signin.SigninService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/signup")
public class SignupController {

	private SignupService signupService;
	
	private SigninService signinService;
	
	@Inject
	public SignupController(SignupService signupService, SigninService signinService) {
		this.signupService = signupService;
		this.signinService = signinService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public SignupForm signupForm() {
		return new SignupForm();
	}

	@RequestMapping(method = RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult formBinding, HttpServletRequest request) {
		if (formBinding.hasErrors()) {
			return null;
		}
		try {
			signupService.signup(form.createPerson());
		} catch (EmailAlreadyOnFileException e) {
			formBinding.rejectValue("email", "already on file");
			return null;
		}
		// TODO this results in a redundant call to the db
		signinService.signin(form.getEmail(), form.getPassword(), new WebAuthenticationDetails(request));
		return "redirect:/";			
	}

}