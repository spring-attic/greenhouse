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
package com.springsource.greenhouse.invite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.ProfileReference;

/**
 * UI Controller for the Twitter invite page.
 * @author Keith Donald
 */
@Controller
public class TwitterInviteController {
	
	private final Twitter twitter;
	
	private final UsersConnectionRepository connectionRepository;

	private final AccountRepository accountRepository;
	
	@Inject
	public TwitterInviteController(Twitter twitter, UsersConnectionRepository connectionRepository, AccountRepository accountRepository) {
		this.twitter = twitter;
		this.connectionRepository = connectionRepository;
		this.accountRepository = accountRepository;
	}
	
	/**
	 * Show the Twitter invite page to the member.
	 * Puts the user's twitter screen name in the model to pre-populate the friend finder form.
	 */
	@RequestMapping(value="/invite/twitter", method=RequestMethod.GET)
	public void friendFinder(Model model) {
		if (twitter.isAuthorized()) {
			model.addAttribute("username", twitter.userOperations().getScreenName());
		}
	}

	/**
	 * Render the list of user's Twitter followers that are already members of the community.
	 * Generally invoked in an Ajax request via JavaScript; if so, renders a partial HTML fragment back.
	 * If JavaScript has been disabled, should re-render the entire invite page with the results populated.
	 */
	@RequestMapping(value="/invite/twitter", method=RequestMethod.POST)
	public String findFriends(@RequestParam String username, Model model) {
		// TODO: progressive enhancement: redirect to a full results page if web request is not an ajax request
		// TODO: consider making a Get request instead of a Post since there are no side effects
		if (StringUtils.hasText(username)) {			
			List<ProfileReference> profileReferences = accountRepository.findProfileReferencesByIds(friendAccountIds(username));
			model.addAttribute("friends", profileReferences);
		}
		return "invite/twitterFriends";
	}
	
	private List<Long> friendAccountIds(String username) {
		List<Long> friendIds = twitter.friendOperations().getFriendIds(username);
		Set<String> providerUserIds = new HashSet<String>(friendIds.size());
		for (Object friendId : friendIds) {
			providerUserIds.add(friendId.toString());
		}
		Set<String> userIds = connectionRepository.findUserIdsConnectedTo("twitter", providerUserIds);
		List<Long> friendAccountIds = new ArrayList<Long>(userIds.size());
		for (String localUserId : userIds) {
			friendAccountIds.add(Long.valueOf(localUserId));
		}
		return friendAccountIds;		
	}

}