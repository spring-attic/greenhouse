package com.springsource.greenhouse.oauth;

import javax.inject.Inject;

import net.sourceforge.wurfl.core.handlers.AppleHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth.provider.ConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetailsService;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ConfirmAccessController {

	@Autowired private AppleHandler appleHandler; 
	
	private OAuthProviderTokenServices tokenServices;

	private ConsumerDetailsService consumerDetailsService;

	@Inject
	public ConfirmAccessController(OAuthProviderTokenServices tokenServices, ConsumerDetailsService consumerDetailsService) {
		this.tokenServices = tokenServices;
		this.consumerDetailsService = consumerDetailsService;
	}

	@RequestMapping(value="/oauth/confirm_access", method=RequestMethod.GET)
	protected String confirmAccessForm(@RequestParam(value="oauth_token", required=true) String oauthToken,
			@RequestParam(value="oauth_callback", required = false) String callback, @RequestHeader("User-Agent") String userAgent, Model model)  {
		
		ConsumerDetails consumer = consumerDetailsService.loadConsumerByConsumerKey(tokenServices.getToken(oauthToken).getConsumerKey());

		model.addAttribute("oauth_token", oauthToken);
		if (callback != null) {
			model.addAttribute("oauth_callback", callback);
		}
		model.addAttribute("consumer", consumer);

		if (appleHandler.canHandle(userAgent)) {
			return "oauth/confirmAccessiPhone";
		} else {
			return "oauth/confirmAccess";
		}
	}

}