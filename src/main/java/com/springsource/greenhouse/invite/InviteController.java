/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.invite;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

/**
 * @author Keith Donald
 */
@Controller
public class InviteController {

	private final InviteRepository inviteRepository;
	
	private final SignupHelper signupHelper;
	
	@Inject
	public InviteController(InviteRepository inviteRepository, AccountRepository accountRepository, SignedUpGateway signupGateway) {
		this.inviteRepository = inviteRepository;
		this.signupHelper = new SignupHelper(accountRepository, signupGateway);
	}

	@RequestMapping(value="/invite", method=RequestMethod.GET)
	public void invitePage(@FacebookUserId String facebookUserId, Model model) {
		model.addAttribute("facebookUserId", facebookUserId);
	}
	
	@RequestMapping(value="/invite/accept", method=RequestMethod.GET)
	public String acceptInvitePage(@RequestParam String token, Model model, HttpServletResponse response) throws IOException {
		try {
			Invite invite = inviteRepository.findInvite(token);
			model.addAttribute(invite.getInvitee());
			model.addAttribute("sentBy", invite.getSentBy());
			model.addAttribute(invite.createSignupForm());
			model.addAttribute("token", token);
			return "invite/accept";
		} catch (InviteException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}

	@RequestMapping(value="/invite/accept", method=RequestMethod.POST)
	public String acceptInvite(final @RequestParam String token, @Valid SignupForm form, BindingResult formBinding, Model model) {
		if (formBinding.hasErrors()) {
			return form(token, model);
		}
		boolean result = signupHelper.signup(form, formBinding, new SignupCallback() {
			public void postCreateAccount(Account account) {
				inviteRepository.markInviteAccepted(token, account);
			}
		});
		return result ? "redirect:/" : form(token, model);
	}

	private String form(String token, Model model) {
		model.addAttribute("token", token);
		return null;
	}

}