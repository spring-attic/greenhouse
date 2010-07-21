package com.springsource.greenhouse.invite;

import org.springframework.social.facebook.FacebookUserId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/invite")
public class InviteController {

	@RequestMapping(method=RequestMethod.GET)
	public void invitePage(@FacebookUserId String facebookUserId, Model model) {
		model.addAttribute("facebookUserId", facebookUserId);
	}
}
