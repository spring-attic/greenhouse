package com.springsource.greenhouse.signup;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.updates.GreenhouseUpdatesService;

@Controller
@RequestMapping("/signup")
public class SignupController {

	private JdbcTemplate jdbcTemplate;
	private GreenhouseUpdatesService updatesService;
	private AuthenticationManager authenticationManager;

	@Inject
	public SignupController(JdbcTemplate jdbcTemplate, GreenhouseUpdatesService updatesService, 
			@Qualifier("org.springframework.security.authenticationManager") ProviderManager authenticationManager) {
		this.jdbcTemplate = jdbcTemplate;
		this.updatesService = updatesService;
		this.authenticationManager = authenticationManager;
	}

	@RequestMapping(method = RequestMethod.GET)
	public SignupForm newForm() {
		return new SignupForm();
	}

	@RequestMapping(method = RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult result, HttpServletRequest request) {
		if (result.hasErrors()) {
			return null;
		}
		jdbcTemplate.update("insert into User (firstName, lastName, email, password) values (?, ?, ?, ?)", 
				form.getFirstName(), form.getLastName(), form.getEmail(), form.getPassword());

		// Add new Greenhouse Update
		String updateText = form.getFirstName() + " " + form.getLastName() + " signed up at the Greenhouse.";
		updatesService.createUpdate(updateText);

		loginAfterSignup(request, form.getEmail(), form.getPassword());
		
		return "redirect:/";
	}

	private void loginAfterSignup(HttpServletRequest request, String username, String password) {
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
			token.setDetails(new WebAuthenticationDetails(request));
			Authentication authentication = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			SecurityContextHolder.getContext().setAuthentication(null);
		}
	}
}
