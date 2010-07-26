package com.springsource.greenhouse.signin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.social.facebook.FacebookAccessToken;
import org.springframework.social.facebook.FacebookOperations;
import org.springframework.social.facebook.FacebookUserId;
import org.springframework.social.facebook.FacebookUserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.utils.SecurityUtils;

@Controller
@RequestMapping("/signin")
public class FacebookSigninController {	
	private static final String SELECT_MEMBER_BY_EMAIL = "select id from Member where email=?";
	private static final String LINK_ACCOUNT_TO_FACEBOOK = 
		"insert into ConnectedAccount (accessToken, member, accountName, secret) values (?, ?, 'facebook', 'facebook')";
	private static final String SELECT_ACCOUNT_INFO = "select id, firstName, lastName, email, username " +
	"from Member, ConnectedAccount where member=id and accessToken=? and accountName='facebook'";
	
	//"insert into ConnectedAccount (accessToken, member, accountName, secret) values (?, ?, ?, ?)
	
	private final JdbcTemplate jdbcTemplate;

	private final FacebookOperations facebook;

	@Inject
	public FacebookSigninController(JdbcTemplate jdbcTemplate, FacebookOperations facebook) {
		this.jdbcTemplate = jdbcTemplate;
		this.facebook = facebook;
	}
	
	@RequestMapping(value="/fb", method=RequestMethod.POST)
	public String signinWithFacebook(@FacebookUserId String facebookUserId, @FacebookAccessToken String accessToken) {				
        List<Account> accounts = findLinkedAccount(accessToken);
        
        if(accounts.size() > 0) {	        	
	        SecurityUtils.signin(accounts.get(0));
	        return "redirect:/";
        } else {        	
        	FacebookUserInfo userInfo = facebook.getUserInfo(accessToken);
        	try {
        		long memberId = jdbcTemplate.queryForLong(SELECT_MEMBER_BY_EMAIL, userInfo.getEmail());
        		jdbcTemplate.update(LINK_ACCOUNT_TO_FACEBOOK, accessToken, memberId);
    			SecurityUtils.signin(findLinkedAccount(accessToken).get(0));
        		return "redirect:/";
        	} catch (DataAccessException e) {
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
