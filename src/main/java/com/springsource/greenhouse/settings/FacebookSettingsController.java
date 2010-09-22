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
import com.springsource.greenhouse.connect.ConnectionDetails;
import com.springsource.greenhouse.connect.FacebookAccountProvider;
import com.springsource.greenhouse.members.ProfilePictureService;

@Controller
@RequestMapping("/settings/facebook")
public class FacebookSettingsController {

	private final FacebookAccountProvider accountProvider;
	
	private final ProfilePictureService profilePictureService;

	@Inject
	public FacebookSettingsController(FacebookAccountProvider accountProvider, ProfilePictureService profilePictureService) {
		this.accountProvider = accountProvider;
		this.profilePictureService = profilePictureService;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String connectView(Account account, @FacebookUserId String facebookUserId, Model model) {
		if (accountProvider.isConnected(account.getId())) {
			model.addAttribute("facebookUserId", facebookUserId);
			return "settings/facebookConnected";
		} else {
			return "settings/facebookConnect";
		}
	}
	
	@RequestMapping(method=RequestMethod.POST) 
	public String connectAccountToFacebook(Account account, @FacebookAccessToken String accessToken,
			@FacebookUserId String facebookUserId, HttpServletRequest request) {
		accountProvider.connect(account.getId(), new ConnectionDetails(accessToken, "", facebookUserId));
		if (request.getParameter("postIt") != null) {
			postToWall(account);
		}
		if (request.getParameter("useFBPic") != null) {
			useFacebookProfilePicture(account, facebookUserId);
		}
		FlashMap.setSuccessMessage("Your Greenhouse account is now connected to your Facebook account!");
		return "redirect:/settings/facebook";			
	}
	
	@RequestMapping(method=RequestMethod.DELETE)
	public String disconnectFacebook(Account account, HttpServletRequest request, Authentication authentication) {
		accountProvider.disconnect(account.getId());
		return "redirect:/settings/facebook";
	}
	
	private void postToWall(Account account) {
		accountProvider.getFacebookApi(account.getId()).postToWall("Join me at the Greenhouse!",
			new FacebookLink(account.getProfileUrl(), "Greenhouse", "Where Spring developers hang out.", 
					"We help you connect with fellow application developers and take advantage of everything the Spring community has to offer."));
	}
	
	private void useFacebookProfilePicture(Account account, String facebookUserId) {
		try {
			FacebookOperations facebookApi = accountProvider.getFacebookApi(account.getId());			
			byte[] imageBytes = facebookApi.getProfilePicture(facebookUserId);
	        profilePictureService.saveProfilePicture(account.getId(), imageBytes);
		} catch (IOException e) {
			FlashMap.setWarningMessage("Greenhouse was unable to use your Facebook profile picture.");
		}
	}
	
}