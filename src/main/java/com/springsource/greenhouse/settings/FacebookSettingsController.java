package com.springsource.greenhouse.settings;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.social.facebook.FacebookAccessToken;
import org.springframework.social.facebook.FacebookUserId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.utils.MemberUtils;

@Controller
@RequestMapping("/settings")
public class FacebookSettingsController {
	private static final String COUNT_FACEBOOK_CONNECTIONS = 
		"select count(*) from ConnectedAccount where member = ? and accountName = 'facebook'";	

	private final JdbcTemplate jdbcTemplate;
	private final AccountRepository accountRepository;

	@Inject
	public FacebookSettingsController(JdbcTemplate jdbcTemplate, AccountRepository accountRepository) {
		this.jdbcTemplate = jdbcTemplate;
		this.accountRepository = accountRepository;
		
	}
	
	@RequestMapping(value="/facebook", method=RequestMethod.GET)
	public String connectView(Account account, @FacebookUserId String facebookUserId, Model model) {
		if (isConnected(account)) {
			model.addAttribute("facebookUserId", facebookUserId);
			return "settings/facebookConnected";
		} else {
			return "settings/facebookConnect";
		}
	}
	
	@RequestMapping(value="/facebook", method=RequestMethod.POST) 
	public String connectAccountToFacebook(HttpServletRequest request, Account account, 
			@FacebookAccessToken String accessToken, @FacebookUserId String facebookId) {
		accountRepository.connectAccount(account.getId(), facebookId, "facebook", accessToken, "facebook");
		
		if(request.getParameter("postIt") != null) {
			postGreenhouseConnectionToWall(request, account, accessToken);
		}
		
		FlashMap.setSuccessMessage("Your Facebook account is now linked to your Greenhouse account!");
		return "redirect:/settings/facebook";
	}

	private void postGreenhouseConnectionToWall(HttpServletRequest request, Account account, String accessToken) {
	    RestTemplate rest = new RestTemplate();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>(); 
		map.set("access_token", accessToken);
		map.set("message", "I just signed into the Greenhouse!");
		map.set("link", MemberUtils.assembleMemberProfileUrl(request, account));
		map.set("name", "Greenhouse");
		map.set("caption", "The place where Spring developers hang out.");
		map.set("description", "We help you connect with fellow developers and take advantage of everything the " +
				"Spring community has to offer.");
		rest.postForLocation("https://graph.facebook.com/me/feed", map);
    }
	
	@RequestMapping(value="/facebook", method=RequestMethod.DELETE)
	public String disconnectFacebook(Account account, HttpServletRequest request, Authentication authentication) {
		accountRepository.removeConnectedAccount(account.getId(), "facebook");
		return "redirect:/settings/facebook";
	}
	
	private boolean isConnected(Account account) {		
		return jdbcTemplate.queryForInt(COUNT_FACEBOOK_CONNECTIONS, account.getId()) == 1;
	}
}
