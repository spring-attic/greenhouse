package com.springsource.greenhouse.members;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springsource.greenhouse.signin.GreenhouseUserDetails;

@Controller
@RequestMapping("/members/*")
public class MembersController {

	private JdbcTemplate jdbcTemplate;
	
	@Inject
	public MembersController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@RequestMapping(value="/@self", headers="Accept=application/json")
	public @ResponseBody Member memberData(GreenhouseUserDetails currentUser) {
		return findMemberByUserId(currentUser.getEntityId());
	}

	@RequestMapping("/{profileKey}")
	public String memberView(@PathVariable String profileKey, Model model) {
		model.addAttribute(findMemberByProfileKey(profileKey));
		return "members/view";
	}

	// internal helpers
	
	private Member findMemberByProfileKey(String profileKey) {
		Long entityId = getEntityId(profileKey);
		return entityId != null ? findMemberByUserId(entityId) : findMemberByUsername(profileKey);
	}
	
	private Member findMemberByUserId(Long userId) {
		return jdbcTemplate.queryForObject("select firstName, lastName from User where id = ?", memberMapper, userId);
	}

	private Member findMemberByUsername(String username) {
		return jdbcTemplate.queryForObject("select firstName, lastName from User where username = ?", memberMapper, username);
	}

	private RowMapper<Member> memberMapper = new RowMapper<Member>() {
		public Member mapRow(ResultSet rs, int row) throws SQLException {
			Member member = new Member();
			member.setFirstName(rs.getString("firstName"));
			member.setLastName(rs.getString("lastName"));
			return member;
		}
	};

	private Long getEntityId(String id) {
		try {
			return Long.parseLong(id);
		} catch (NumberFormatException e) {
			return null;
		}		
	}

}
