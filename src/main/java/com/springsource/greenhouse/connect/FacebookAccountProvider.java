package com.springsource.greenhouse.connect;

import org.springframework.social.facebook.FacebookOperations;

public interface FacebookAccountProvider extends AccountProvider {

	Long getAppId();

	FacebookOperations getFacebookApi(Long accountId);

}
