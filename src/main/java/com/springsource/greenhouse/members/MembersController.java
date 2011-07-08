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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springsource.greenhouse.account.Account;

/**
 * UI Controllers for managing public member profiles.
 * @author Keith Donald
 * @author Craig Walls
 */
@Controller
@RequestMapping("/members")
public class MembersController {
	
	private final ProfileRepository profileRepository;

	@Inject
	public MembersController(ProfileRepository profileRepository) {
		this.profileRepository = profileRepository;
	}

	/**
	 * Write the currently signed-in member's profile to the response as JSON.
	 */
	@RequestMapping(value="/@self", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Profile profile(Account account) {
		return profileRepository.findByAccountId(account.getId());
	}

	/**
	 * Render the requested member's profile as HTML in the user's web browser.
	 * The profile page is accessible to the general public and does not require signin to view.
	 */
	@RequestMapping(value="/{profileKey}", method=RequestMethod.GET)
	public String profileView(@PathVariable String profileKey, Model model) {
		Profile profile = profileRepository.findById(profileKey);
		model.addAttribute(profile);
		model.addAttribute("connectedProfiles", profileRepository.findConnectedProfiles(profile.getAccountId()));
		model.addAttribute("metadata", buildFacebookOpenGraphMetadata(profile));
		model.addAttribute("facebookAppId", facebookAppId);
		return "members/view";
	}
		
	// internal helpers

	// this metadata is required by Facebook's "Like" widgets and included in the page by meta tags in page header
	private Map<String, String> buildFacebookOpenGraphMetadata(Profile profile) {
		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put("og:title", profile.getDisplayName());
		metadata.put("og:type", "public_figure");
		// TODO Greenhouse is hardcoded here
		metadata.put("og:site_name", "Greenhouse");
		metadata.put("fb:app_id", facebookAppId);
		return metadata;		
	}
	
	@Value("#{environment['facebook.appId']}")
	private String facebookAppId; 

}
