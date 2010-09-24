package com.springsource.greenhouse.invite;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.social.facebook.FacebookOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.connect.AccountProvider;

@Controller
@RequestMapping("/invite/facebook")
public class FacebookInviteController {

	private final AccountProvider<FacebookOperations> accountProvider;
	
	@Inject
	public FacebookInviteController(AccountProvider<FacebookOperations> accountProvider) {
		this.accountProvider = accountProvider;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public void friendFinder(Model model, Account account) {
		if (accountProvider.isConnected(account.getId())) {
			List<String> providerAccountIds = accountProvider.getApi(account.getId()).getFriendIds();
			model.addAttribute("friendAccounts", accountProvider.findAccountsWithProviderAccountIds(providerAccountIds));
		} else {
			model.addAttribute("friendAccounts", Collections.emptySet());
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, params="skip=1")
	public String skipInvitation() {
		return "redirect:/invite";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String handleInvitations(@RequestParam("ids[]") String[] inviteIds) {
		FlashMap.setSuccessMessage("Your invitations have been sent");
		return "redirect:/invite";
	}
	
}