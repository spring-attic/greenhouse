package com.springsource.greenhouse.connect;

import org.springframework.social.facebook.FacebookOperations;

public interface FacebookAccountProvider extends AccountProvider {

	FacebookOperations getFacebookApi(Long accountId);

	Long getAppId();
	
}
