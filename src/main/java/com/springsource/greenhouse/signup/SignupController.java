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

import com.springsource.greenhouse.signin.GreenhouseUserDetails;
import com.springsource.greenhouse.signin.GreenhouseUserDetailsService;
import com.springsource.greenhouse.signup.mail.SignupMessage;
import com.springsource.greenhouse.signup.mail.SignupMessageGateway;
import com.springsource.greenhouse.updates.GreenhouseUpdatesService;

@Controller
@RequestMapping("/signup")
public class SignupController {

	private JdbcTemplate jdbcTemplate;
	private GreenhouseUpdatesService updatesService;
	private GreenhouseUserDetailsService userDetailsService;
	private AuthenticationManager authenticationManager;
	private SignupMessageGateway signupMessageGateway;

	@Inject
	public SignupController(JdbcTemplate jdbcTemplate, GreenhouseUpdatesService updatesService, 
			GreenhouseUserDetailsService userDetailsService,
			@Qualifier("org.springframework.security.authenticationManager") ProviderManager authenticationManager,
			SignupMessageGateway signupMessageGateway) {
		this.jdbcTemplate = jdbcTemplate;
		this.updatesService = updatesService;
		this.userDetailsService = userDetailsService;
		this.authenticationManager = authenticationManager;
		this.signupMessageGateway = signupMessageGateway;
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
		
		int userId = jdbcTemplate.queryForInt("select id from User where email = ?", form.getEmail());
		
		postUpdate(form.getFirstName(), form.getLastName(), form.getEmail());

		signupMessageGateway.publish(new SignupMessage(userId, form.getEmail(), form.getFirstName()));
		
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
	
	private void postUpdate(String firstName, String lastName, String email) {
		// Retrieve the user that was just added
		GreenhouseUserDetails details = (GreenhouseUserDetails) userDetailsService.loadUserByUsername(email);
		
		// Add an update that the user joined
		String updateText = firstName + " " + lastName + " signed up at the Greenhouse.";
		updatesService.createUpdate(updateText, details);
	}
}
