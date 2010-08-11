package com.springsource.greenhouse.members;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springsource.greenhouse.account.Account;

@Controller
@RequestMapping("/members/*")
public class MembersController {
	
	private ProfileRepository profileRepository;

	@Value("#{facebookProperties.applicationId}")
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
		model.addAttribute("metadata", buildOpenGraphMetadata(profile));
		return "members/view";
	}
	
	private Map<String, String> buildOpenGraphMetadata(Profile profile) {
		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put("og:title", profile.getDisplayName());
		metadata.put("og:type", "public_figure");
		metadata.put("og:site_name", "Greenhouse");
		metadata.put("fb:app_id", facebookAppId);
		return metadata;		
	}
}
