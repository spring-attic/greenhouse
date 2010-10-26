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
import com.springsource.greenhouse.connect.ServiceProvider;

@Controller
@RequestMapping("/invite/facebook")
public class FacebookInviteController {

	private final ServiceProvider<FacebookOperations> facebookProvider;
	
	@Inject
	public FacebookInviteController(ServiceProvider<FacebookOperations> facebookProvider) {
		this.facebookProvider = facebookProvider;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public void friendFinder(Model model, Account account) {
		if (facebookProvider.isConnected(account.getId())) {
			List<String> providerAccountIds = facebookProvider.getServiceOperations(account.getId()).getFriendIds();
			model.addAttribute("friendAccounts", facebookProvider.findAccountsConnectedTo(providerAccountIds));
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