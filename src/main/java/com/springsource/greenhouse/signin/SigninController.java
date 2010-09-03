package com.springsource.greenhouse.signin;

import org.springframework.mobile.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/signin")
public class SigninController {
	
	@RequestMapping(method=RequestMethod.GET)
	public String signin(Device device) {
		return device.isApple() ? "signin-iphone" : "signin";
	}
}
