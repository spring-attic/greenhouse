package com.springsource.greenhouse.members;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springsource.greenhouse.account.Account;

@Controller
@RequestMapping("/members/*")
public class MembersController {

	private MemberRepository membersService;

	@Inject
	public MembersController(MemberRepository membersService) {
		this.membersService = membersService;
	}

	@RequestMapping(value = "/@self", headers = "Accept=application/json")
	public @ResponseBody
	Member memberData(Account account) {
		return membersService.findMemberByAccountId(account.getId());
	}

	@RequestMapping("/{profileKey}")
	public String memberView(@PathVariable String profileKey, Model model) {
		model.addAttribute(membersService.findMemberByProfileKey(profileKey));
		model.addAttribute("connectedIds", membersService.lookupConnectedAccountIds(profileKey));
		return "members/view";
	}
}
