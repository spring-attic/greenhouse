package com.springsource.greenhouse.oauth.provider;

import javax.inject.Inject;

import org.springframework.mobile.Device;
import org.springframework.security.oauth.provider.ConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetailsService;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

// TODO consider refactoring to remove dependency on Spring Security OAuth APIs
@Controller
public class ConfirmAccessController {

	private OAuthProviderTokenServices tokenServices;

	private ConsumerDetailsService consumerDetailsService;

	@Inject
	public ConfirmAccessController(OAuthProviderTokenServices tokenServices, ConsumerDetailsService consumerDetailsService) {
		this.tokenServices = tokenServices;
		this.consumerDetailsService = consumerDetailsService;
	}

	@RequestMapping(value="/oauth/confirm_access", method=RequestMethod.GET)
	protected String confirmAccessForm(@RequestParam(value="oauth_token", required=true) String oauthToken,
			@RequestParam(value="oauth_callback", required = false) String callback, Device device, Model model)  {
		ConsumerDetails consumer = consumerDetailsService.loadConsumerByConsumerKey(tokenServices.getToken(oauthToken).getConsumerKey());
		model.addAttribute("oauth_token", oauthToken);
		if (callback != null) {
			model.addAttribute("oauth_callback", callback);
		}
		model.addAttribute("consumer", consumer);
		return device.isApple() ? "oauth/confirmAccess-iphone" : "oauth/confirmAccess";
	}

}