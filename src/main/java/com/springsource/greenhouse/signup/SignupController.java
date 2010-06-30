package com.springsource.greenhouse.signup;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.jdbc.core.JdbcTemplate;
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
	
	@Inject
	public SignupController(JdbcTemplate jdbcTemplate, GreenhouseUpdatesService updatesService) {
		this.jdbcTemplate = jdbcTemplate;
		this.updatesService = updatesService;
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
		
		// Add new Greenhouse Update
		String updateText = form.getFirstName() + " " + form.getLastName() + " signed up at the Greenhouse.";
		updatesService.createUpdate(updateText);
		
		return "redirect:/signin";
	}

}
