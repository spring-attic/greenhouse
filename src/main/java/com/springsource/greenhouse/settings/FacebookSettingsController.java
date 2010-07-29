package com.springsource.greenhouse.settings;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.social.facebook.FacebookAccessToken;
import org.springframework.social.facebook.FacebookLink;
import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.facebook.FacebookUserId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.utils.MemberUtils;

@Controller
@RequestMapping("/settings")
public class FacebookSettingsController {
	private final AccountRepository accountRepository;
	private final FacebookOperations facebook;

	@Inject
	public FacebookSettingsController(AccountRepository accountRepository, FacebookOperations facebook) {
		this.accountRepository = accountRepository;
		this.facebook = facebook;
	}
	
	@RequestMapping(value="/facebook", method=RequestMethod.GET)
	public String connectView(Account account, @FacebookUserId String facebookUserId, Model model) {
		if (accountRepository.isConnected(account.getId(), "facebook")) {
			model.addAttribute("facebookUserId", facebookUserId);
			return "settings/facebookConnected";
		} else {
			return "settings/facebookConnect";
		}
	}
	
	@RequestMapping(value="/facebook", method=RequestMethod.POST) 
	public String connectAccountToFacebook(HttpServletRequest request, Account account, 
			@FacebookAccessToken String accessToken, @FacebookUserId String facebookId) {
		
		if(StringUtils.hasText(accessToken)) {			
			accountRepository.connectAccount(account.getId(), facebookId, "facebook", accessToken, "facebook");
			
			if(request.getParameter("postIt") != null) {
				postGreenhouseConnectionToWall(request, account, accessToken);
			}
			
			FlashMap.setSuccessMessage("Your Facebook account is now linked to your Greenhouse account!");
		}
		return "redirect:/settings/facebook";
	}

	private void postGreenhouseConnectionToWall(HttpServletRequest request, Account account, String accessToken) {
		facebook.postToWall(accessToken, "I just signed into the Greenhouse!", 
				new FacebookLink(
						MemberUtils.assembleMemberProfileUrl(request, account), 
						"Greenhouse", "The place where Spring developers hang out.", 
						"We help you connect with fellow developers and take advantage of everything the " +
							"Spring community has to offer."));
    }
	
	@RequestMapping(value="/facebook", method=RequestMethod.DELETE)
	public String disconnectFacebook(Account account, HttpServletRequest request, Authentication authentication) {
		accountRepository.removeConnectedAccount(account.getId(), "facebook");
		return "redirect:/settings/facebook";
	}
}
