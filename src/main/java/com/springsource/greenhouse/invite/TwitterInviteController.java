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

import java.util.List;

import javax.inject.Inject;

import org.springframework.social.twitter.TwitterOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.connect.ServiceProvider;

/**
 * UI Controller for the Twitter invite page.
 * @author Keith Donald
 */
@Controller
public class TwitterInviteController {
	
	private ServiceProvider<TwitterOperations> twitterProvider;
	
	@Inject
	public TwitterInviteController(ServiceProvider<TwitterOperations> twitterProvider) {
		this.twitterProvider = twitterProvider;
	}
	
	/**
	 * Show the Twitter invite page to the member.
	 * Puts the user's twitter screen name in the model to pre-populate the friend finder form.
	 */
	@RequestMapping(value="/invite/twitter", method=RequestMethod.GET)
	public void friendFinder(Account account, Model model) {
		String twitterId = twitterProvider.getProviderAccountId(account.getId());	
		if (twitterId != null) {			
			model.addAttribute("username", twitterId);
		}
	}

	/**
	 * Render the list of user's Twitter followers that are already members of the community.
	 * Generally invoked in an Ajax request via JavaScript; if so, renders a partial HTML fragment back.
	 * If JavaScript has been disabled, should re-render the entire invite page with the results populated.
	 */
	@RequestMapping(value="/invite/twitter", method=RequestMethod.POST)
	// TODO: progressive enhancement: redirect to a full results page if web request is not an ajax request
	// TODO: consider making a Get request instead of a Post since there are no side effects
	public String findFriends(@RequestParam String username, Account account, Model model) {
		if (StringUtils.hasText(username)) {
			List<String> screenNames = twitterProvider.getServiceOperations(account.getId()).getFriends(username);
			model.addAttribute("friends", twitterProvider.findMembersConnectedTo(screenNames));
		}
		return "invite/twitterFriends";
	}
	
}