package com.springsource.greenhouse.invite.facebook;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.social.facebook.FacebookAccessToken;
import org.springframework.social.facebook.FacebookOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.invite.GreenhouseFriend;

@Controller
@RequestMapping("/invite/facebook")
public class FacebookInviteController {
	private static final String SELECT_GREENHOUSE_FB_FRIENDS = 
		"select username, firstName, lastName from Member, ConnectedAccount where member = id and " +
		"accountName='facebook' and externalId in ( :ids )";
	private final FacebookOperations facebook;
	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Inject
	public FacebookInviteController(FacebookOperations facebook, JdbcTemplate jdbcTemplate) {
		this.facebook = facebook;
		this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public void inviteFriends(@FacebookAccessToken String accessToken, Model model) {
		List<String> friendIds = facebook.getFriendIds(accessToken);		
		model.addAttribute("friends", findGreenhouseFacebookFriends(friendIds));
	}
	
	@RequestMapping(method=RequestMethod.GET, params="skip=1")
	public String skipInvitation() {
		return "redirect:/invite";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String handleInvitations(@RequestParam("ids[]") String[] inviteIds) {
		FlashMap.setSuccessMessage("Your invitations have been sent!");
		return "redirect:/invite";
	}
	
	private List<GreenhouseFriend> findGreenhouseFacebookFriends(List<String> facebookFriends) {
	    return jdbcTemplate.query(SELECT_GREENHOUSE_FB_FRIENDS, Collections.singletonMap("ids", facebookFriends),
	    		new RowMapper<GreenhouseFriend>() {
					public GreenhouseFriend mapRow(ResultSet rs, int rowNum) throws SQLException {
						GreenhouseFriend friend = new GreenhouseFriend();
						friend.setUsername(rs.getString("username"));
						friend.setName(rs.getString("firstName") + " " + rs.getString("lastName"));
					    return friend;
					}
				});
	}
}
