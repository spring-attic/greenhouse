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
import com.springsource.greenhouse.connect.ServiceProvider;
import com.springsource.greenhouse.members.ProfilePictureService;

/**
 * Facebook-specific UI Controller for connecting member Accounts with Facebook.
 * @author Keith Donald
 */
@Controller
public class FacebookConnectController {

	private final ServiceProvider<FacebookOperations> facebookProvider;
	
	private final ProfilePictureService profilePictureService;

	@Inject
	public FacebookConnectController(ServiceProvider<FacebookOperations> facebookProvider, ProfilePictureService profilePictureService) {
		this.facebookProvider = facebookProvider;
		this.profilePictureService = profilePictureService;
	}
	
	@RequestMapping(value="/connect/facebook", method=RequestMethod.GET)
	public String connectView(Account account, @FacebookUserId String facebookUserId, Model model) {
		if (facebookProvider.isConnected(account.getId())) {
			model.addAttribute("facebookUserId", facebookUserId);
			return "connect/facebookConnected";
		} else {
			return "connect/facebookConnect";
		}
	}
	
	@RequestMapping(value="/connect/facebook", method=RequestMethod.POST) 
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
	
	@RequestMapping(value="/connect/facebook", method=RequestMethod.DELETE)
	public String disconnectFacebook(Account account, HttpServletRequest request, Authentication authentication) {
		facebookProvider.disconnect(account.getId());
		return "redirect:/connect/facebook";
	}
	
	// internal helpers
	
	private void postToWall(FacebookOperations api, Account account) {
		api.updateStatus("Join me at the Greenhouse!", new FacebookLink(account.getProfileUrl(), "Greenhouse", "Where Spring developers hang out.",
			"We help you connect with fellow application developers and take advantage of everything the Spring community has to offer."));
	}
	
	private void useFacebookProfilePicture(FacebookOperations api, Account account, String facebookUserId) {
		try {
			byte[] imageBytes = api.getProfilePicture(facebookUserId);
	        profilePictureService.saveProfilePicture(account.getId(), imageBytes);
		} catch (IOException e) {
			FlashMap.setWarningMessage("Greenhouse was unable to use your Facebook profile picture.");
		}
	}
	
}