package com.springsource.greenhouse.members;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.account.Account;
import org.springframework.social.account.PictureSize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/members/*")
public class MembersController {
	
	private ProfileRepository profileRepository;

	@Value("${facebook.applicationId}")
	private String facebookAppId; 

	@Inject
	public MembersController(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}

	@RequestMapping(value = "/@self", headers = "Accept=application/json")
	public @ResponseBody Profile profile(Account account) {
		return profileRepository.findByAccountId(account.getId());
	}

	@RequestMapping("/{profileKey}")
	public String profileView(@PathVariable String profileKey, Model model) {
		Profile profile = profileRepository.findByKey(profileKey);
		model.addAttribute(profile);
		model.addAttribute("connectedProfiles", profileRepository.findConnectedProfiles(profile.getAccountId()));
		model.addAttribute("metadata", buildFacebookOpenGraphMetadata(profile));
		return "members/view";
	}
	
	@RequestMapping("/{profileKey}/picture")
	public String profilePicture(@PathVariable String profileKey, @RequestParam(required=false) PictureSize size) {
		return "redirect:" + profileRepository.findProfilePictureUrl(profileKey, size);
	}
	
	private Map<String, String> buildFacebookOpenGraphMetadata(Profile profile) {
		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put("og:title", profile.getDisplayName());
		metadata.put("og:type", "public_figure");
		metadata.put("og:site_name", "Greenhouse");
		metadata.put("fb:app_id", facebookAppId);
		return metadata;		
	}
}
