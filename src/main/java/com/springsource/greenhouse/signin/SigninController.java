package com.springsource.greenhouse.signin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/signin")
public class SigninController {
	
	@RequestMapping(method=RequestMethod.GET)
	public void signin() {
	}
}
