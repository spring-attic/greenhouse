package com.springsource.greenhouse.invite;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/invite/mail")
public class MailInviteController {

	@RequestMapping(method=RequestMethod.GET)
	public MailInviteForm invitePage() {
		return new MailInviteForm();
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String sendInvites(@Valid MailInviteForm form, BindingResult result) {
		if (result.hasErrors()) {
			return null;
		}
		return "redirect:/invite/mail";
	}

}
