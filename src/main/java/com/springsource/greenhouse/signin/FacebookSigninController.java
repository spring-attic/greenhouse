package com.springsource.greenhouse.signin;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.social.facebook.FacebookUserId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.utils.SecurityUtils;

@Controller
@RequestMapping("/signin")
public class FacebookSigninController {
	
	private static final String SELECT_ACCOUNT_INFO = "select id, firstName, lastName, email, username " +
			"from Member, ConnectedMember where member=id and connectionId=? and connectionApp='facebook'";
	private final JdbcTemplate jdbcTemplate;

	@Inject
	public FacebookSigninController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;		
	}
	
	@RequestMapping(value="/fb", method=RequestMethod.POST)
	public String signinWithFacebook(@FacebookUserId String facebookUserId) {
		try {
	        Account account = jdbcTemplate.queryForObject(SELECT_ACCOUNT_INFO, new RowMapper<Account>() {
	        	public Account mapRow(ResultSet rs, int row) throws SQLException {				
	                return new Account(rs.getLong("id"), rs.getString("firstName"), rs.getString("lastName"),
	                		rs.getString("email"), rs.getString("username"));
	            }
	        }, facebookUserId);

	        SecurityUtils.signin(account);
	        return "redirect:/";
        } catch (IncorrectResultSizeDataAccessException e) {
        	// TODO: For GREENHOUSE-163, we could retrieve the user's info from Facebook and use it to
        	//       automatically register a user and log them into Greenhouse

    		FlashMap.setErrorMessage(
    				"You are currently logged into Facebook as <fb:name linked='false' useyou='false' uid='" + 
    				facebookUserId + "'></fb:name>. This account is not linked to your Greenhouse account.");

        	return "redirect:/signin";
        }
	}

}
