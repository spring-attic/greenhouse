package com.springsource.greenhouse.signin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.social.facebook.FacebookAccessToken;
import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.facebook.FacebookUserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.utils.SecurityUtils;

@Controller
@RequestMapping("/signin")
public class FacebookSigninController {	
	private static final String COUNT_MEMBERS_BY_EMAIL = "select count(id) from Member where email=?";
	private static final String SELECT_ACCOUNT_INFO = "select id, firstName, lastName, email, username " +
		"from Member, ConnectedAccount where member=id and accessToken=? and accountName='facebook'";
		
	private final JdbcTemplate jdbcTemplate;
	private final FacebookOperations facebook;

	@Inject
	public FacebookSigninController(JdbcTemplate jdbcTemplate, FacebookOperations facebook) {
		this.jdbcTemplate = jdbcTemplate;
		this.facebook = facebook;
	}
	
	@RequestMapping(value="/fb", method=RequestMethod.POST)
	public String signinWithFacebook(@FacebookAccessToken String accessToken) {				
        List<Account> accounts = findLinkedAccount(accessToken);
        
        if(accounts.size() > 0) {	        	
	        SecurityUtils.signin(accounts.get(0));
	        return "redirect:/";
        } else {        	
        	FacebookUserInfo userInfo = facebook.getUserInfo(accessToken);
    		int count = jdbcTemplate.queryForInt(COUNT_MEMBERS_BY_EMAIL, userInfo.getEmail());
    		if(count > 0) {
	    		FlashMap.setWarningMessage("It looks like your Facebook profile is not linked to your Greenhouse " +
	    				"profile. To connect them, sign in and then go to the settings page.");
	    		
	    		return "redirect:/signin";
    		} else {
        		return "redirect:/signup/fb";        			
    		}
        }
	}

	private List<Account> findLinkedAccount(String accessToken) {
	    List<Account> accounts = jdbcTemplate.query(SELECT_ACCOUNT_INFO, new RowMapper<Account>() {
        	public Account mapRow(ResultSet rs, int row) throws SQLException {				
                return new Account(rs.getLong("id"), rs.getString("firstName"), rs.getString("lastName"),
                		rs.getString("email"), rs.getString("username"));
            }
        }, accessToken);
	    return accounts;
    }
}
