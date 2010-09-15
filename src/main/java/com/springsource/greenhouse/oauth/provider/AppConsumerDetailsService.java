package com.springsource.greenhouse.oauth.provider;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth.common.OAuthException;
import org.springframework.security.oauth.common.signature.SharedConsumerSecret;
import org.springframework.security.oauth.common.signature.SignatureSecret;
import org.springframework.security.oauth.provider.ConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetailsService;

import com.springsource.greenhouse.account.InvalidApiKeyException;
import com.springsource.greenhouse.develop.App;
import com.springsource.greenhouse.develop.AppRepository;

public class AppConsumerDetailsService implements ConsumerDetailsService {

	private AppRepository appRepository;
	
	@Inject
	public AppConsumerDetailsService(AppRepository appRepository) {
		this.appRepository = appRepository;
	}

	public ConsumerDetails loadConsumerByConsumerKey(final String key) throws OAuthException {
		try {
			return consumerDetailsFor(appRepository.findAppByApiKey(key));
		} catch (InvalidApiKeyException e) {
			throw new OAuthException("Invalid OAuth consumer key " + key, e);
		}
	}

	private ConsumerDetails consumerDetailsFor(App app) {
		return new AppConsumerDetails(app);
	}
	
	@SuppressWarnings("serial")
	private static class AppConsumerDetails implements ConsumerDetails {

		private App app;

		public AppConsumerDetails(App app) {
			this.app = app;
		}

		public String getConsumerName() {
			return app.getSummary().getName();
		}

		public String getConsumerKey() {
			return app.getApiKey();
		}

		@Override
		public SignatureSecret getSignatureSecret() {
			return new SharedConsumerSecret(app.getSecret());
		}
		
		public List<GrantedAuthority> getAuthorities() {
			return Collections.emptyList();
		}
		
	}

}