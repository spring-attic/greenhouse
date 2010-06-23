package com.springsource.greenhouse;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

	@RequestMapping(value="/", method=RequestMethod.GET)
	public String home(Principal user) {
		return user == null ? "homeNotSignedIn" : "homeSignedIn";
	}
}
