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
import com.springsource.greenhouse.account.ConnectedAccountNotFoundException;
import com.springsource.greenhouse.account.UsernameNotFoundException;
import com.springsource.greenhouse.utils.SecurityUtils;

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
	
	@RequestMapping(value="/fb", method=RequestMethod.POST)
	public String signinWithFacebook(@FacebookAccessToken String accessToken) {				
		try {
			Account account = accountRepository.findByConnectedAccount(accessToken, "facebook");
	        SecurityUtils.signin(account);
	        return "redirect:/";
		} catch (ConnectedAccountNotFoundException e) {
        	return handleUnlinkedAccount(accessToken);
		}
	}

	private String handleUnlinkedAccount(String accessToken) {
	    try {
		    FacebookUserInfo userInfo = facebook.getUserInfo(accessToken);		    
	        accountRepository.findByUsername(userInfo.getEmail());
	    	FlashMap.setWarningMessage("It looks like your Facebook profile is not linked to your Greenhouse " +
	    		"profile. To connect them, sign in and then go to the settings page.");

	    	return "redirect:/signin";
	    } catch (UsernameNotFoundException e1) {
	    	FlashMap.setInfoMessage("Your Facebook account is not linked to any Greenhouse profile. " +
	    			"Please complete the following form to create a Greenhouse account. The form has " +
	    			"been prefilled with information from your Facebook profile.");
	    	return "redirect:/signup/fb";        			
	    }
    }
}
