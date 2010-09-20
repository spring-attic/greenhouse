package com.springsource.greenhouse.signin;

import javax.inject.Inject;
import javax.inject.Provider;

import org.springframework.social.facebook.FacebookAccessToken;
import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.facebook.FacebookUserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.AccountUtils;
import com.springsource.greenhouse.account.InvalidAccessTokenException;
import com.springsource.greenhouse.account.UsernameNotFoundException;
import com.springsource.greenhouse.signup.SignupForm;

@Controller
@RequestMapping("/signin/facebook")
public class FacebookSigninController {

	private final Provider<FacebookOperations> facebookApi;

	private final AccountRepository accountRepository;

	@Inject
	public FacebookSigninController(Provider<FacebookOperations> facebookApi, AccountRepository accountRepository) {
		this.facebookApi = facebookApi;
		this.accountRepository = accountRepository;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String signin(@FacebookAccessToken String accessToken) {
		try {
			Account account = accountRepository.findByAccountConnection("facebook", accessToken);
			AccountUtils.signin(account);
			return "redirect:/";
		} catch (InvalidAccessTokenException e) {
			return handleConnectedAccountNotFound(facebookApi.get().getUserInfo());
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public String signupForm(Model model) {
		FacebookUserInfo userInfo = facebookApi.get().getUserInfo();
		SignupForm signupForm = new SignupForm();
		signupForm.setFirstName(userInfo.getFirstName());
		signupForm.setLastName(userInfo.getLastName());
		signupForm.setEmail(userInfo.getEmail());
		model.addAttribute(signupForm);
		return "signup";
	}
	
	// internal helpers
	
	private String handleConnectedAccountNotFound(FacebookUserInfo userInfo) {
		try {
			accountRepository.findByUsername(userInfo.getEmail());
			FlashMap.setWarningMessage("Your Facebook account is not linked with your Greenhouse account. "
					+ "To connect them, sign in and then go to the Settings page.");
			return "redirect:/signin";
		} catch (UsernameNotFoundException e) {
			FlashMap.setInfoMessage("Your Facebook account is not linked with a Greenhouse account. "
					+ "If you do not have a Greenhouse account, complete the following form to create one. "
					+ "If you already have an account, sign in with your username and password, then go to the Settings page to connect with Facebook.");
			// TODO how is this going to work if the account is not already linked (see GET handler above)			
			return "redirect:/signup/facebook";
		}
	}
}
