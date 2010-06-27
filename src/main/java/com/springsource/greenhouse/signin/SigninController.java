package com.springsource.greenhouse.signin;

import net.sourceforge.wurfl.core.Device;
import net.sourceforge.wurfl.core.WURFLManager;
import net.sourceforge.wurfl.core.handlers.AppleHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.oauth.BasicRedirectStrategy;

@Controller
@RequestMapping("/signin")
public class SigninController {
	
	private static final Logger logger = LoggerFactory.getLogger(BasicRedirectStrategy.class);
	
	@Autowired private WURFLManager wurflManager;
	@Autowired private AppleHandler appleHandler;
	
	@RequestMapping(method=RequestMethod.GET)
	public String signin(@RequestHeader("User-Agent") String userAgent) {
		
		// this is just some test code, playing with the library
		Device device = wurflManager.getDeviceForRequest(userAgent);
		logger.debug("Device: " + device.getId());
		logger.debug("Mobile Browser: " + device.getCapability("mobile_browser"));
		
		// this is a trivial use of the WURFL library, but it is all we need right now.
		boolean b = appleHandler.canHandle(userAgent);
		logger.debug("Apple device: " + b);
		
		if (b) {
			return "signin-iphone";
		}
		else {
			return "signin";
		}
	}
}
