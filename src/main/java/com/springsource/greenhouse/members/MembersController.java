package com.springsource.greenhouse.members;

import java.util.HashMap;
import java.util.Map;

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
		Member member = membersService.findMemberByProfileKey(profileKey);
		model.addAttribute(member);
		model.addAttribute("connectedIds", membersService.lookupConnectedAccountIds(profileKey));
		model.addAttribute("metadata", buildOpenGraphMetadata(member));
		return "members/view";
	}
	
	private Map<String, String> buildOpenGraphMetadata(Member member) {
		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put("og:title", member.getDisplayName());
		metadata.put("og:type", "public_figure");
		metadata.put("og:site_name", "Greenhouse");
		metadata.put("fb:app_id", "140372495981006"); // TODO: Probably shouldn't hardcode this
		return metadata;		
	}
}
