package com.springsource.greenhouse.members;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/members/*")
public class MembersController {

	@RequestMapping(value="/@self", headers="Accept=application/json")
	public @ResponseBody Member memberData() {
		return new Member();
	}

	@RequestMapping("/{profileKey}")
	public String memberView(@PathVariable String profileKey, Model model) {
		model.addAttribute(getMember(profileKey));
		return "members/view";
	}

	// internal helpers
	
	private Member getMember(String profileKey) {
		Long entityId = getEntityId(profileKey);
		return entityId != null ? new Member() : new Member();
	}
	
	private Long getEntityId(String id) {
		try {
			return Long.parseLong(id);
		} catch (NumberFormatException e) {
			return null;
		}		
	}
}
