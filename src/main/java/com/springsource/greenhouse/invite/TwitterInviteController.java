package com.springsource.greenhouse.invite;

import java.util.List;

import javax.inject.Inject;

import org.springframework.social.twitter.TwitterOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.connect.AccountProvider;

@Controller
@RequestMapping("/invite/twitter")
public class TwitterInviteController {
	
	private AccountProvider<TwitterOperations> accountProvider;
	
	@Inject
	public TwitterInviteController(AccountProvider<TwitterOperations> accountProvider) {
		this.accountProvider = accountProvider;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public void friendFinder(Account account, Model model) {
		String twitterId = accountProvider.getProviderAccountId(account.getId());	
		if (twitterId != null) {			
			model.addAttribute("username", twitterId);
		}
	}

	@RequestMapping(method=RequestMethod.POST)
	public String findFriends(@RequestParam String username, Account account, Model model) {
		if (StringUtils.hasText(username)) {
			List<String> screenNames = accountProvider.getApi(account.getId()).getFollowed(username);
			model.addAttribute("friendAccounts", accountProvider.findAccountsWithProviderAccountIds(screenNames));
		}
		return "invite/twitterFriends";
	}
	
}