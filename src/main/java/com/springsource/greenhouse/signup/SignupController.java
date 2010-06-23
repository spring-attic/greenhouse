package com.springsource.greenhouse.signup;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/signup")
public class SignupController {
	
	private JdbcTemplate jdbcTemplate;
	
	@Inject
	public SignupController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
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
		return "redirect:/signin";
	}

}
