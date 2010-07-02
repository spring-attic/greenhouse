package com.springsource.greenhouse.settings;

import java.io.PrintWriter;
import java.io.StringWriter;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;

import com.springsource.greenhouse.oauth.OAuthUtil;
import com.springsource.greenhouse.signin.GreenhouseUserDetails;

@Controller
public class TwitterController {
	private OAuthUtil oauthUtil;
	private TwitterService twitterService;
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Inject
	public TwitterController(OAuthUtil oauthUtil, TwitterService twitterService, JdbcTemplate jdbcTemplate) {
		this.oauthUtil = oauthUtil;
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
		OAuthConsumerToken accessToken = oauthUtil. getAccessToken("twitter", request, authentication);
		String[] twitterFriends = twitterService.getFriends(accessToken, twitterName);
		model.put("friends", findGreenhouseTwitterFriends(twitterFriends));
		return "twitter/friends";
	}
	
	@ExceptionHandler(HttpStatusCodeException.class)
	public String handleIOException(HttpStatusCodeException ex, HttpServletRequest request) {
		request.setAttribute("error", ex.getMessage());
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		ex.printStackTrace(printWriter);
		request.setAttribute("stackTrace", stringWriter.getBuffer().toString());
		return "twitter/error";
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
}
