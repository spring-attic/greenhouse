package com.springsource.greenhouse.invite;

import java.util.List;

import javax.inject.Inject;

import org.springframework.social.facebook.FacebookAccessToken;
import org.springframework.social.facebook.FacebookOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.AccountRepository;

@Controller
@RequestMapping("/invite/facebook")
public class FacebookInviteController {

	private final FacebookOperations facebook;
	
	private final AccountRepository accountRepository;
	
	@Inject
	public FacebookInviteController(FacebookOperations facebook, AccountRepository accountRepository) {
		this.facebook = facebook;
		this.accountRepository = accountRepository;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public void friendFinder(@FacebookAccessToken String accessToken, Model model) {
		List<String> friendIds = facebook.getFriendIds(accessToken);
		model.addAttribute("friendAccounts", accountRepository.findFriendAccounts("facebook", friendIds));
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