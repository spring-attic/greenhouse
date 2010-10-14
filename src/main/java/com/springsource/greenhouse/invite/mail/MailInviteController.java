package com.springsource.greenhouse.invite.mail;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.templating.ResourceStringTemplateFactory;
import org.springframework.templating.StringTemplate;
import org.springframework.templating.StringTemplateFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.springsource.greenhouse.account.Account;

@Controller
@RequestMapping("/invite/mail")
public class MailInviteController {

	private final StringTemplateFactory inviteTemplateFactory = new ResourceStringTemplateFactory(new ClassPathResource("invite-text-body.st", getClass()));

	private final MailInviteService inviteService;
 
	@Inject
	public MailInviteController(MailInviteService inviteService) {
		this.inviteService = inviteService;
	}

	@RequestMapping(method=RequestMethod.GET)
	public MailInviteForm invitePage(Account account) {
		MailInviteForm form = new MailInviteForm();
		form.setInvitationText(renderStandardInvitationText(account));
		return form;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String sendInvites(@Valid MailInviteForm form, BindingResult result, Account account) {
		if (result.hasErrors()) {
			return null;
		}
		inviteService.sendInvite(account, form.getInvitees(),  form.getInvitationText());
		return "redirect:/invite/mail";
	}
	
	private String renderStandardInvitationText(Account account) {
		StringTemplate template = inviteTemplateFactory.getStringTemplate();
		template.put("account", account);
		return template.render();
	}

}
