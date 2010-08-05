package com.springsource.greenhouse.members;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springsource.greenhouse.account.Account;

@Controller
@RequestMapping("/members/*")
public class MembersController {
	private static final String BASE_CONNECTED_ID_QUERY = "select provider, accountid from connectedaccount, member where member = id ";
	private static final String USERNAME_CLAUSE = "and username = ?";
	private static final String ID_CLAUSE = "and id = ?";
	
	private MemberRepository membersService;
	private final JdbcTemplate jdbcTemplate;
	
	@Inject
	public MembersController(MemberRepository membersService, JdbcTemplate jdbcTemplate) {
		this.membersService = membersService;
		this.jdbcTemplate = jdbcTemplate;
	}

	@RequestMapping(value="/@self", headers="Accept=application/json")
	public @ResponseBody Member memberData(Account account) {
		return membersService.findMemberByAccountId(account.getId());
	}

	@RequestMapping("/{profileKey}")
	public String memberView(@PathVariable String profileKey, Model model) {
		model.addAttribute(membersService.findMemberByProfileKey(profileKey));
		model.addAttribute("connectedIds", lookupConnectedAccountIds(profileKey));
		return "members/view";
	}
	
	Map<String, String> lookupConnectedAccountIds(String profileKey) {
		final Map<String, String> connectedIds = new HashMap<String, String>();

		Object key = profileKey;
		String query = BASE_CONNECTED_ID_QUERY + USERNAME_CLAUSE; 
		try {
			key = Long.parseLong(profileKey);
			query = BASE_CONNECTED_ID_QUERY + ID_CLAUSE;
		} catch (NumberFormatException e) {}
		
		jdbcTemplate.query(query,
				new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				connectedIds.put(rs.getString("provider"), rs.getString("accountId"));
			}
		}, key);
		
		return connectedIds;
	}	
}
