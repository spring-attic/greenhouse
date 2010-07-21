package com.springsource.greenhouse.invite.facebook;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/invite/facebook")
public class FacebookInviteController {
	@RequestMapping(method=RequestMethod.GET)
	public void inviteFriends() {	
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String handleInvitations() {
		return "redirect:/invite";
	}	
}
