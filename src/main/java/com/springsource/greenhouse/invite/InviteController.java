package com.springsource.greenhouse.invite;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.social.facebook.FacebookUserId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.signup.SignedUpGateway;
import com.springsource.greenhouse.signup.SignupForm;
import com.springsource.greenhouse.signup.SignupHelper;
import com.springsource.greenhouse.signup.SignupHelper.SignupCallback;

@Controller
@RequestMapping("/invite")
public class InviteController {

	private final InviteRepository inviteRepository;
	
	private final SignupHelper signupHelper;
	
	@Inject
	public InviteController(InviteRepository inviteRepository, AccountRepository accountRepository, SignedUpGateway signupGateway) {
		this.inviteRepository = inviteRepository;
		this.signupHelper = new SignupHelper(accountRepository, signupGateway);
	}

	@RequestMapping(method=RequestMethod.GET)
	public void invitePage(@FacebookUserId String facebookUserId, Model model) {
		model.addAttribute("facebookUserId", facebookUserId);
	}
	
	@RequestMapping(value="/accept", method=RequestMethod.GET, params="token")
	public String acceptInvitePage(@RequestParam String token, Model model, HttpServletResponse response) throws IOException {
		try {
			Invite invite = inviteRepository.getInvite(token);
			model.addAttribute("token", token);
			model.addAttribute(invite.getInvitee());
			model.addAttribute("sentBy", invite.getSentBy());
			model.addAttribute(invite.createSignupForm());
			return "invite/accept";
		} catch (InviteException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}

	@RequestMapping(value="/accept", method=RequestMethod.POST)
	public String acceptInvite(final @RequestParam String token, SignupForm form, BindingResult formBinding) {
		if (formBinding.hasErrors()) {
			return null;
		}
		boolean result = signupHelper.signup(form, formBinding, new SignupCallback() {
			public void postCreateAccount(Account account) {
				inviteRepository.markInviteAccepted(token, account);
			}
		});
		return result ? "redirect:/" : null;
	}

}