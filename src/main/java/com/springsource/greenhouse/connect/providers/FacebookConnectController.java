package com.springsource.greenhouse.connect.providers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.connect.ServiceProvider;
import com.springsource.greenhouse.members.ProfilePictureService;

@Controller
@RequestMapping("/connect/facebook")
public class FacebookConnectController {

	private final ServiceProvider<FacebookOperations> facebookProvider;
	
	private final ProfilePictureService profilePictureService;

	private final AccountRepository accountRepository;

	@Inject
	public FacebookConnectController(ServiceProvider<FacebookOperations> facebookProvider, ProfilePictureService profilePictureService, AccountRepository accountRepository) {
		this.facebookProvider = facebookProvider;
		this.profilePictureService = profilePictureService;
		this.accountRepository = accountRepository;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String connectView(Account account, @FacebookUserId String facebookUserId, Model model) {
		if (facebookProvider.isConnected(account.getId())) {
			model.addAttribute("facebookUserId", facebookUserId);
			return "connect/facebookConnected";
		} else {
			return "connect/facebookConnect";
		}
	}
	
	@RequestMapping(method=RequestMethod.POST) 
	public String connectAccountToFacebook(Account account, @FacebookAccessToken String accessToken, @FacebookUserId String facebookUserId,
			@RequestParam(required=false, defaultValue="false") boolean postToWall, @RequestParam(required=false, defaultValue="false") boolean useProfilePicture) {
		if (facebookUserId != null && accessToken != null) {
			facebookProvider.addConnection(account.getId(), accessToken, facebookUserId);
			FacebookOperations api = facebookProvider.getServiceOperations(account.getId());
			if (postToWall) {
				postToWall(api, account);
			}
			if (useProfilePicture) {
				useFacebookProfilePicture(api, account, facebookUserId);
			}
			FlashMap.setSuccessMessage("Your Greenhouse account is now connected to your Facebook account!");
		}
		return "redirect:/connect/facebook";
	}
	
	@RequestMapping(method=RequestMethod.DELETE)
	public String disconnectFacebook(Account account, HttpServletRequest request, Authentication authentication) {
		facebookProvider.disconnect(account.getId());
		return "redirect:/connect/facebook";
	}
	
	private void postToWall(FacebookOperations api, Account account) {
		api.postToWall("Join me at the Greenhouse!", new FacebookLink(account.getProfileUrl(), "Greenhouse", "Where Spring developers hang out.", 
			"We help you connect with fellow application developers and take advantage of everything the Spring community has to offer."));
	}
	
	private void useFacebookProfilePicture(FacebookOperations api, Account account, String facebookUserId) {
		try {
			byte[] imageBytes = api.getProfilePicture(facebookUserId);
	        profilePictureService.saveProfilePicture(account.getId(), imageBytes);
			accountRepository.markProfilePictureSet(account.getId());
		} catch (IOException e) {
			FlashMap.setWarningMessage("Greenhouse was unable to use your Facebook profile picture.");
		}
	}
	
}