package com.springsource.greenhouse.connect;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.social.twitter.TwitterApi;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.Account;


public class TwitterConnectInterceptorTest {
	
	private MockHttpServletRequest httpServletRequest;
	
	private TwitterConnectInterceptor interceptor;
	
	private ServletWebRequest request;

	@Before
	public void setup() {
		interceptor = new TwitterConnectInterceptor();
		httpServletRequest = new MockHttpServletRequest();
		request = new ServletWebRequest(httpServletRequest);
	}

	@Test
	public void preConnect() {
		httpServletRequest.setParameter("postTweet", "true");
		interceptor.preConnect(null, request);
		Boolean postTweetAttributeValue = (Boolean) request.getAttribute("twitterConnect.postTweet", WebRequest.SCOPE_SESSION);
		assertNotNull(postTweetAttributeValue);
		assertTrue(postTweetAttributeValue);
	}

	@Test
	public void preConnect_noPostTweetParameter() {
		interceptor.preConnect(null, request);
		Boolean postTweetAttributeValue = (Boolean) request.getAttribute("twitterConnect.postTweet", WebRequest.SCOPE_SESSION);
		assertNull(postTweetAttributeValue);
	}

	@Test
	public void postConnect() {
		request.setAttribute("twitterConnect.postTweet", Boolean.TRUE, WebRequest.SCOPE_SESSION);
		Account account = new Account(2L, "Craig", "Walls", "cwalls@vmware.com", "habuma", "http://picture.com/url",
				new UriTemplate("http://greenhouse.springsource.org/members/{profile}"));
		request.setAttribute("account", account, WebRequest.SCOPE_REQUEST);
		StubTwitterApi twitterApi = new StubTwitterApi();
		StubServiceProviderConnection<TwitterApi> connection = new StubServiceProviderConnection<TwitterApi>(twitterApi);
		interceptor.postConnect(null, connection, request);
		twitterApi.verifyStatus("Join me at the Greenhouse! http://greenhouse.springsource.org/members/habuma");
	}

	@Test
	public void postConnect_noPostTweetAttribute() {
		Account account = new Account(2L, "Craig", "Walls", "cwalls@vmware.com", "habuma", "http://picture.com/url",
				new UriTemplate("http://greenhouse.springsource.org/members/{profile}"));
		request.setAttribute("account", account, WebRequest.SCOPE_REQUEST);
		StubTwitterApi twitterApi = new StubTwitterApi();
		StubServiceProviderConnection<TwitterApi> connection = new StubServiceProviderConnection<TwitterApi>(twitterApi);
		interceptor.postConnect(null, connection, request);
		twitterApi.verifyStatus(null);
	}

}