package com.springsource.greenhouse.signup;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.signin.GreenhouseUserDetails;
import com.springsource.greenhouse.signin.GreenhouseUserDetailsService;
import com.springsource.greenhouse.updates.GreenhouseUpdatesService;

@Controller
@RequestMapping("/signup")
public class SignupController {
	
	private JdbcTemplate jdbcTemplate;
	private GreenhouseUpdatesService updatesService;
	private GreenhouseUserDetailsService userDetailsService;
	
	@Inject
	public SignupController(JdbcTemplate jdbcTemplate, GreenhouseUserDetailsService userDetailsService, GreenhouseUpdatesService updatesService) {
		this.jdbcTemplate = jdbcTemplate;
		this.updatesService = updatesService;
		this.userDetailsService = userDetailsService;
	}

	@RequestMapping(method=RequestMethod.GET)
	public SignupForm newForm() {
		return new SignupForm();
	}

	@RequestMapping(method=RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult result) {
		if (result.hasErrors()) {
			return null;
		}
		jdbcTemplate.update("insert into User (firstName, lastName, email, password) values (?, ?, ?, ?)", form.getFirstName(), form.getLastName(), form.getEmail(), form.getPassword());
		
		// Retrieve the user that was just added
		GreenhouseUserDetails details = (GreenhouseUserDetails) userDetailsService.loadUserByUsername(form.getEmail());
		
		// Add an update that the user joined
		String updateText = form.getFirstName() + " " + form.getLastName() + " signed up at the Greenhouse.";
		updatesService.createUpdate(updateText, details);
						
		return "redirect:/signin";
	}

}
