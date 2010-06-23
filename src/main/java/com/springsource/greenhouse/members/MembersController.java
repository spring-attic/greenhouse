package com.springsource.greenhouse.members;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/members/*")
public class MembersController {

	@RequestMapping(value="/{id}", headers="Accept=application/json")
	public @ResponseBody Member memberData(@PathVariable String id) {
		return getMember(id);
	}

	@RequestMapping("/{id}")
	public String memberView(@PathVariable String id, Model model) {
		model.addAttribute(getMember(id));
		return "members/view";
	}

	// internal helpers
	
	private Member getMember(String id) {
		Long entityId = getEntityId(id);
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
