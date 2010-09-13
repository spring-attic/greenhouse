package com.springsource.greenhouse.settings;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.social.facebook.FacebookAccessToken;
import org.springframework.social.facebook.FacebookLink;
import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.facebook.FacebookUserId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountAlreadyConnectedException;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.members.ProfilePictureService;

@Controller
@RequestMapping("/settings")
public class FacebookSettingsController {
	
	private final AccountRepository accountRepository;
	
	private final FacebookOperations facebook;
	
	private final ProfilePictureService profilePictureService;

	@Inject
	public FacebookSettingsController(AccountRepository accountRepository, FacebookOperations facebook, ProfilePictureService profilePictureService) {
		this.accountRepository = accountRepository;
		this.facebook = facebook;
		this.profilePictureService = profilePictureService;
	}
	
	@RequestMapping(value="/facebook", method=RequestMethod.GET)
	public String connectView(Account account, @FacebookUserId String facebookUserId, Model model) {
		if (accountRepository.hasConnectedAccount(account.getId(), FACEBOOK_PROVIDER)) {
			model.addAttribute("facebookUserId", facebookUserId);
			return "settings/facebookConnected";
		} else {
			return "settings/facebookConnect";
		}
	}
	
	@RequestMapping(value="/facebook", method=RequestMethod.POST) 
	public String connectAccountToFacebook(HttpServletRequest request, Account account, @FacebookAccessToken String accessToken, @FacebookUserId String facebookUserId) {
		try {
			accountRepository.connectAccount(account.getId(), FACEBOOK_PROVIDER, accessToken, facebookUserId);		
			if (request.getParameter("postIt") != null) {
				postGreenhouseConnectionToWall(request, account, accessToken);
			}			
			if (request.getParameter("useFBPic") != null) {
				useFacebookProfilePicture(account, accessToken);
			}
			FlashMap.setSuccessMessage("Your Greenhouse account is now connected to your Facebook account!");
		} catch (AccountAlreadyConnectedException e) {
			FlashMap.setErrorMessage("Unable to connect: Your Facebook account is already connected to a Greenhouse account.");
		}
		return "redirect:/settings/facebook";			
	}

	private void postGreenhouseConnectionToWall(HttpServletRequest request, Account account, String accessToken) {
		facebook.postToWall(accessToken, "Join me at the Greenhouse!", 
			new FacebookLink(account.getProfileUrl(), "Greenhouse", "Where Spring developers hang out.", 
					"We help you connect with fellow application developers and take advantage of everything the Spring community has to offer."));
    }
	
	@RequestMapping(value="/facebook", method=RequestMethod.DELETE)
	public String disconnectFacebook(Account account, HttpServletRequest request, Authentication authentication) {
		accountRepository.disconnectAccount(account.getId(), FACEBOOK_PROVIDER);
		return "redirect:/settings/facebook";
	}
	
	public void useFacebookProfilePicture(Account account, String accessToken) {
		try {
	        byte[] imageBytes = facebook.getProfilePicture(accessToken);	        
	        profilePictureService.saveProfilePicture(account.getId(), imageBytes);
	        accountRepository.markProfilePictureSet(account.getId());
		} catch (IOException e) {
			FlashMap.setWarningMessage("Greenhouse was unable to use your Facebook profile picture.");
		}
	}
	
	private static final String FACEBOOK_PROVIDER = "Facebook";

}