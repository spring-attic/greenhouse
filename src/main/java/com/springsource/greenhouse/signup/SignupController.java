package com.springsource.greenhouse.signup;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("/signup")
public class SignupController {

	private JdbcTemplate jdbcTemplate;

	private AuthenticationManager authenticationManager;
	
	private SignupMessageGateway signupMessageGateway;

	@Inject
	// TODO eliminate the Qualifier
	public SignupController(JdbcTemplate jdbcTemplate, @Qualifier("org.springframework.security.authenticationManager") AuthenticationManager authenticationManager, SignupMessageGateway signupMessageGateway) {
		this.jdbcTemplate = jdbcTemplate;
		this.authenticationManager = authenticationManager;
		this.signupMessageGateway = signupMessageGateway;
	}

	@RequestMapping(method = RequestMethod.GET)
	public SignupForm signupForm() {
		return new SignupForm();
	}

	@RequestMapping(method = RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult result, HttpServletRequest request) {
		if (result.hasErrors()) {
			return null;
		}
		jdbcTemplate.update("insert into User (firstName, lastName, email, password) values (?, ?, ?, ?)",
				form.getFirstName(), form.getLastName(), form.getEmail(), form.getPassword());
		Long userId = jdbcTemplate.queryForLong("call identity");
		signupMessageGateway.publish(new SignupMessage(userId, form.getFirstName(), form.getLastName(), form.getEmail()));
		signIn(form.getEmail(), form.getPassword(), request);
		return "redirect:/";
	}

	// internal helpers
	
	private void signIn(String username, String password, HttpServletRequest request) {
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
			token.setDetails(new WebAuthenticationDetails(request));
			Authentication authentication = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (AuthenticationException e) {
			SecurityContextHolder.getContext().setAuthentication(null);
		}
	}
	
}
