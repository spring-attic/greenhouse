package com.springsource.greenhouse.signup;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.account.Account;

@Controller
@RequestMapping("/signup")
public class SignupController {

	private SignupService signupService;
	
	@Inject
	public SignupController(SignupService signupService) {
		this.signupService = signupService;
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
			Account account = signupService.signup(form.createPerson());
			signin(account);
		} catch (EmailAlreadyOnFileException e) {
			formBinding.rejectValue("email", "account.duplicateEmail", "already on file");
			return null;
		}
		return "redirect:/";			
	}

	private void signin(Account account) {
		List<GrantedAuthority> authorities = null;
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(account, null, authorities);
		SecurityContextHolder.getContext().setAuthentication(token);
	}
	
}