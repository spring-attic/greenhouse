package com.springsource.greenhouse.invite.facebook;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.flash.FlashMap;

@Controller
@RequestMapping("/invite/facebook")
public class FacebookInviteController {
	@RequestMapping(method=RequestMethod.GET)
	public void inviteFriends() {	
	}
	
	@RequestMapping(method=RequestMethod.GET, params="skip=1")
	public String skipInvitation() {
		return "redirect:/invite";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String handleInvitations(@RequestParam("ids[]") String[] inviteIds) {
		FlashMap.setSuccessMessage("Your invitations have been sent!");
		return "redirect:/invite";
	}	
}
