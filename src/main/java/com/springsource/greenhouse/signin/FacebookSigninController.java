package com.springsource.greenhouse.signin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.social.facebook.FacebookUserId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.account.Account;

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
		List<Account> accounts = jdbcTemplate.query(SELECT_ACCOUNT_INFO, new RowMapper<Account>() {
			public Account mapRow(ResultSet rs, int row) throws SQLException {				
	            return new Account(rs.getLong("id"), rs.getString("firstName"), rs.getString("lastName"),
	            		rs.getString("email"), rs.getString("username"));
            }
		}, facebookUserId);
		
		if(accounts.size() > 0) {
			PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(accounts.get(0), null, new ArrayList<GrantedAuthority>());
			SecurityContextHolder.getContext().setAuthentication(token);
		}
		
		return "redirect:/";
	}
}
