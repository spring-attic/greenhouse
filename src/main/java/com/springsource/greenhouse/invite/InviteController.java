package com.springsource.greenhouse.invite;

import javax.inject.Inject;

import org.springframework.social.facebook.FacebookUserId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/invite")
public class InviteController {

	private InviteRepository inviteRepository;
	
	@Inject
	public InviteController(InviteRepository inviteRepository) {
		this.inviteRepository = inviteRepository;
	}

	@RequestMapping(method=RequestMethod.GET)
	public void invitePage(@FacebookUserId String facebookUserId, Model model) {
		model.addAttribute("facebookUserId", facebookUserId);
	}
	
	@RequestMapping(method=RequestMethod.GET, params="token")
	public String acceptInvite(@RequestParam String token, Model model) {
		InviteDetails inviteDetails = inviteRepository.getInviteDetails(token);
		model.addAttribute("sentBy", inviteDetails.getSentBy());
		model.addAttribute(inviteDetails.createSignupForm());
		return "invite/accept";
	}

}
