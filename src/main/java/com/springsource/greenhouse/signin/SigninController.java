package com.springsource.greenhouse.signin;

import net.sourceforge.wurfl.core.Device;
import net.sourceforge.wurfl.core.handlers.AppleHandler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/signin")
public class SigninController {
	
	@RequestMapping(method=RequestMethod.GET)
	public String signin(Device device) {
		if (new AppleHandler().canHandle(device.getUserAgent())) {
			return "signin-iphone";
		}
		else {
			return "signin";
		}
	}
}
