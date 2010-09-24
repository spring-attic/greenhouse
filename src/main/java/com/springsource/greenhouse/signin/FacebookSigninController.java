package com.springsource.greenhouse.signin;

import javax.inject.Inject;

import org.springframework.social.facebook.FacebookAccessToken;
import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.facebook.FacebookTemplate;
import org.springframework.social.facebook.FacebookUserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.AccountUtils;
import com.springsource.greenhouse.account.UsernameNotFoundException;
import com.springsource.greenhouse.connect.AccountProvider;
import com.springsource.greenhouse.connect.NoSuchAccountConnectionException;

@Controller
@RequestMapping("/signin/facebook")
public class FacebookSigninController {

	private final AccountProvider<FacebookOperations> accountProvider;

	private final AccountRepository accountRepository;

	@Inject
	public FacebookSigninController(AccountProvider<FacebookOperations> accountProvider, AccountRepository accountRepository) {
		this.accountProvider = accountProvider;
		this.accountRepository = accountRepository;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String signin(@FacebookAccessToken String accessToken) {
		try {
			Account account = accountProvider.findAccountByConnection(accessToken);
			AccountUtils.signin(account);
			return "redirect:/";
		} catch (NoSuchAccountConnectionException e) {
			return handleNoFacebookConnection(new FacebookTemplate(accessToken).getUserInfo());
		}
	}
	
	// internal helpers
	
	private String handleNoFacebookConnection(FacebookUserInfo userInfo) {
		try {
			accountRepository.findByUsername(userInfo.getEmail());
			FlashMap.setWarningMessage("Your Facebook account is not linked with your Greenhouse account. To connect them, sign in and then go to the Settings page.");
			return "redirect:/signin";
		} catch (UsernameNotFoundException e) {
			FlashMap.setInfoMessage("Your Facebook account is not linked with a Greenhouse account. "
					+ "If you do not have a Greenhouse account, complete the following form to create one. "
					+ "If you already have an account, sign in with your username and password, then go to the Settings page to connect with Facebook.");
			// TODO encode form query parameters into url to pre-populate from userinfo
			return "redirect:/signup";
		}
	}
}
