package com.springsource.greenhouse.settings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.consumer.token.OAuthConsumerToken;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServices;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServicesFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.springsource.greenhouse.signin.GreenhouseUserDetails;

@Controller
public class TwitterController {
	private OAuthConsumerTokenServicesFactory tokenServicesFactory;
	private TwitterService twitterService;
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Inject
	public TwitterController(OAuthConsumerTokenServicesFactory tokenServicesFactory, TwitterService twitterService,
			JdbcTemplate jdbcTemplate) {
		this.tokenServicesFactory = tokenServicesFactory;
		this.twitterService = twitterService;		
		this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
	}
	
	@RequestMapping(value="/import/twitter", method=RequestMethod.GET)
	public String displayFriendFinderForm(HttpServletRequest request, Authentication authentication, 
			Map<String, Object> model) {
		GreenhouseUserDetails greenhouseUser = (GreenhouseUserDetails) authentication.getPrincipal();
		String username = greenhouseUser.getUsername() != null ? greenhouseUser.getUsername() : "";
		model.put("twitterName", username);
		return "twitter/friendSearch";
	}
	
	@RequestMapping(value="/import/twitter", method=RequestMethod.POST)
	public String findFriends(HttpServletRequest request, Authentication authentication, @RequestParam("twitterName") String twitterName,
			Map<String, Object> model) {
		OAuthConsumerToken accessToken = getAccessToken(request, authentication);
		String[] twitterFriends = twitterService.getFriends(accessToken, twitterName);
		model.put("friends", findGreenhouseTwitterFriends(twitterFriends));
		return "twitter/friends";
	}
	
	private List<GreenhouseFriend> findGreenhouseTwitterFriends(String[] twitterFriends) {
	    return jdbcTemplate.query(
	    		"select username, firstName, lastName from User where username in ( :names )",
	    		Collections.singletonMap("names", Arrays.asList(twitterFriends)),
	    		new RowMapper<GreenhouseFriend>() {
					public GreenhouseFriend mapRow(ResultSet rs, int rowNum) throws SQLException {
						GreenhouseFriend friend = new GreenhouseFriend();
						friend.setUsername(rs.getString("username"));
						friend.setName(rs.getString("firstName") + " " + rs.getString("lastName"));
					    return friend;
					}
				});
    }
	
	// TODO: This is duplicated here and in TwitterSettingsController. Find a common place for it.
	private OAuthConsumerToken getAccessToken(HttpServletRequest request, Authentication authentication) {
		OAuthConsumerTokenServices tokenServices = tokenServicesFactory.getTokenServices(authentication, request);
		OAuthConsumerToken accessToken = tokenServices.getToken("twitter");
		return accessToken;
	}
}
