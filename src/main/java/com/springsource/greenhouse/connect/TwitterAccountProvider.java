package com.springsource.greenhouse.connect;

import org.springframework.social.twitter.TwitterOperations;

public interface TwitterAccountProvider extends AccountProvider {

	TwitterOperations getTwitterApi(Long accountId);

}
