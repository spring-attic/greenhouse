package com.springsource.greenhouse.develop;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/develop/apps/*")
public class AppsController {

	@RequestMapping(method=RequestMethod.GET)
	void appList() {
		
	}

	@RequestMapping(value="/register", method=RequestMethod.GET)
	void registerForm() {
		
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	void register(@Valid AppRegistrationForm app, BindingResult result) {
		if (result.hasErrors()) {
			return;
		}
	}

}
