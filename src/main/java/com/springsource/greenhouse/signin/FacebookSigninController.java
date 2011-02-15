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
package com.springsource.greenhouse.signin;

import javax.inject.Inject;

import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.facebook.FacebookProfile;
import org.springframework.social.facebook.FacebookTemplate;
import org.springframework.social.facebook.web.FacebookCookieValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.AccountUtils;
import com.springsource.greenhouse.account.SignInNotFoundException;
import com.springsource.greenhouse.connect.NoSuchAccountConnectionException;
import com.springsource.greenhouse.connect.ServiceProvider;

/**
 * UI Controller that allows you to signin with your Facebook account, if your local member account has been connected to Facebook.
 * @author Keith Donald
 * @author Craig Walls
 */
@Controller
public class FacebookSigninController {

	private final ServiceProvider<FacebookOperations> facebookProvider;

	private final AccountRepository accountRepository;

	@Inject
	public FacebookSigninController(ServiceProvider<FacebookOperations> facebookProvider, AccountRepository accountRepository) {
		this.facebookProvider = facebookProvider;
		this.accountRepository = accountRepository;
	}

	/**
	 * Sign in the member with their Facebook account.
	 * The submitted access token, obtained via a cookie, identifies the connection between a local member account and a Facebook account.
	 * For sign-in to succeed, the submitted access token must match a Facebook accessToken on file in our system.
	 * Notifies the user if the access token is invalid.
	 */
	@RequestMapping(value="/signin/facebook", method=RequestMethod.POST)
	public String signin(@FacebookCookieValue("access_token") String accessToken) {
		try {
			Account account = facebookProvider.findAccountByConnection(accessToken);
			AccountUtils.signin(account);
			return "redirect:/";
		} catch (NoSuchAccountConnectionException e) {
			return handleNoFacebookConnection(new FacebookTemplate(accessToken).getUserProfile());
		}
	}
	
	// internal helpers
	
	private String handleNoFacebookConnection(FacebookProfile userInfo) {
		try {
			accountRepository.findBySignin(userInfo.getEmail());
			FlashMap.setWarningMessage("Your Facebook account is not linked with your Greenhouse account. To connect them, sign in and then go to the Settings page.");
			return "redirect:/signin";
		} catch (SignInNotFoundException e) {
			FlashMap.setInfoMessage("Your Facebook account is not linked with a Greenhouse account. "
					+ "If you do not have a Greenhouse account, complete the following form to create one. "
					+ "If you already have an account, sign in with your username and password, then go to the Settings page to connect with Facebook.");
			// TODO encode form query parameters into url to pre-populate from userinfo
			return "redirect:/signup";
		}
	}
}
