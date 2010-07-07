package com.springsource.greenhouse.invites;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/invite")
public class InvitesController {

	@RequestMapping(method=RequestMethod.GET)
	public void invitePage() {
		
	}
}
