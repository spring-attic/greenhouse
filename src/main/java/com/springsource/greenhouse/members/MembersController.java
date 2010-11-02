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
package com.springsource.greenhouse.members;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.PictureSize;

/**
 * @author Keith Donald
 */
@Controller
public class MembersController {
	
	private final ProfileRepository profileRepository;

	@Inject
	public MembersController(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}

	@RequestMapping(value="/members/@self", headers="Accept=application/json")
	public @ResponseBody Profile profile(Account account) {
		return profileRepository.findByAccountId(account.getId());
	}

	@RequestMapping("/members/{profileKey}")
	public String profileView(@PathVariable String profileKey, Model model) {
		Profile profile = profileRepository.findByKey(profileKey);
		model.addAttribute(profile);
		model.addAttribute("connectedProfiles", profileRepository.findConnectedProfiles(profile.getAccountId()));
		model.addAttribute("metadata", buildFacebookOpenGraphMetadata(profile));
		return "members/view";
	}
	
	@RequestMapping("/members/{profileKey}/picture")
	public String profilePicture(@PathVariable String profileKey, @RequestParam(required=false) PictureSize size) {
		return "redirect:" + profileRepository.findProfilePictureUrl(profileKey, size);
	}
	
	// internal helpers
	
	private Map<String, String> buildFacebookOpenGraphMetadata(Profile profile) {
		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put("og:title", profile.getDisplayName());
		metadata.put("og:type", "public_figure");
		metadata.put("og:site_name", "Greenhouse");
		metadata.put("fb:app_id", facebookAppId);
		return metadata;		
	}
	
	@Value("#{facebookProvider.appId}")
	private String facebookAppId; 

}
