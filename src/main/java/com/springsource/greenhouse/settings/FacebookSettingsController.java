package com.springsource.greenhouse.settings;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.social.facebook.FacebookAccessToken;
import org.springframework.social.facebook.FacebookUserId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.account.Account;

@Controller
@RequestMapping("/settings")
public class FacebookSettingsController {
	private static final String DELETE_FACEBOOK_CONNECTION = 
		"delete from ConnectedAccount where member=? and accountName='facebook'";
	private static final String COUNT_FACEBOOK_CONNECTIONS = 
		"select count(*) from ConnectedAccount where member = ? and accountName = 'facebook'";
	
	private static final String LINK_ACCOUNT_TO_FACEBOOK = 
		"insert into ConnectedAccount (accessToken, member, accountName, secret) values (?, ?, 'facebook', 'facebook')";

	private final JdbcTemplate jdbcTemplate;

	@Inject
	public FacebookSettingsController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		
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
	public String connectAccountToFacebook(Account account, @FacebookAccessToken String accessToken) {		
		jdbcTemplate.update(LINK_ACCOUNT_TO_FACEBOOK, accessToken, account.getId());
		return "redirect:/settings/facebook";
	}
	
	@RequestMapping(value="/facebook", method=RequestMethod.DELETE)
	public String disconnectTwitter(Account account, HttpServletRequest request, Authentication authentication) {
		jdbcTemplate.update(DELETE_FACEBOOK_CONNECTION, account.getId());
		return "redirect:/settings/facebook";
	}
	
	private boolean isConnected(Account account) {
		return jdbcTemplate.queryForInt(COUNT_FACEBOOK_CONNECTIONS, account.getId()) == 1;
	}
}
