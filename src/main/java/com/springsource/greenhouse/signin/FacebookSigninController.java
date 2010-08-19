package com.springsource.greenhouse.signin;

import javax.inject.Inject;

import org.springframework.social.facebook.FacebookAccessToken;
import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.facebook.FacebookUserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.AccountUtils;
import com.springsource.greenhouse.account.ConnectedAccountNotFoundException;
import com.springsource.greenhouse.account.UsernameNotFoundException;

@Controller
@RequestMapping("/signin")
public class FacebookSigninController {

	private final FacebookOperations facebook;

	private final AccountRepository accountRepository;

	@Inject
	public FacebookSigninController(FacebookOperations facebook, AccountRepository accountRepository) {
		this.facebook = facebook;
		this.accountRepository = accountRepository;
	}

	@RequestMapping(value = "/fb", method = RequestMethod.POST)
	public String signinWithFacebook(@FacebookAccessToken String accessToken) {
		try {
			Account account = accountRepository.findByConnectedAccount("facebook", accessToken);
			AccountUtils.signin(account);
			return "redirect:/";
		} catch (ConnectedAccountNotFoundException e) {
			return handleConnectedAccountNotFound(facebook.getUserInfo(accessToken));
		} // TODO handle case where accessToken is invalid
	}

	private String handleConnectedAccountNotFound(FacebookUserInfo userInfo) {
		try {
			accountRepository.findByUsername(userInfo.getEmail());
			FlashMap.setWarningMessage("Your Facebook account is not linked with your Greenhouse account. "
					+ "To connect them, sign in and then go to the Settings page.");
			return "redirect:/signin";
		} catch (UsernameNotFoundException e1) {
			FlashMap.setInfoMessage("Your Facebook account is not linked with a Greenhouse account. "
					+ "If you do not have a Greenhouse account, complete the following form to create one. "
					+ "If you already have an account, sign in with your username and password, then go to the Settings page to connect with Facebook.");
			return "redirect:/signup/fb";
		}
	}
}
