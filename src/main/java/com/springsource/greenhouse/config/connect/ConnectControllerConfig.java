package com.springsource.greenhouse.config.connect;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.support.ConnectionRepository;
import org.springframework.social.facebook.web.FacebookSigninController;
import org.springframework.social.web.connect.ConnectController;
import org.springframework.social.web.connect.ConnectInterceptor;
import org.springframework.social.web.connect.SignInControllerGateway;

import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.connect.FacebookConnectInterceptor;
import com.springsource.greenhouse.connect.TwitterConnectInterceptor;
import com.springsource.greenhouse.members.ProfilePictureService;
import com.springsource.greenhouse.signin.AccountSigninGateway;

@Configuration
public class ConnectControllerConfig {

	@Bean
	public ConnectController connectController(@Value("#{environment['application.secureUrl']}") String applicationUrl,
			ProfilePictureService profilePictureService) {
		ConnectController controller = new ConnectController(applicationUrl);
		List<ConnectInterceptor<?>> interceptors = new ArrayList<ConnectInterceptor<?>>();
		interceptors.add(new FacebookConnectInterceptor(profilePictureService));
		interceptors.add(new TwitterConnectInterceptor());
		controller.setInterceptors(interceptors);
		return controller;
	}
	
	@Bean
	public FacebookSigninController facebookSigninController(@Value("#{environment['facebook.appId']}") String appId,
			@Value("#{environment['facebook.appSecret']}") String appSecret,
			ConnectionRepository connectionRepository, AccountRepository accountRepository) {
		SignInControllerGateway signinGateway = new AccountSigninGateway(accountRepository);
		return new FacebookSigninController(connectionRepository, signinGateway, appId, appSecret);
	}
	
}
