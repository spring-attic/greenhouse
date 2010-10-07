package com.springsource.greenhouse.connect;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.social.twitter.TwitterOperations;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.connect.providers.TwitterConnectInterceptor;

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
		Boolean postTweetAttributeValue = (Boolean) request.getAttribute("twitterConnect.postTweet",
				WebRequest.SCOPE_SESSION);
		assertNotNull(postTweetAttributeValue);
		assertTrue(postTweetAttributeValue);
	}

	@Test
	public void preConnect_noPostTweetParameter() {
		interceptor.preConnect(null, request);
		Boolean postTweetAttributeValue = (Boolean) request.getAttribute("twitterConnect.postTweet",
				WebRequest.SCOPE_SESSION);
		assertNull(postTweetAttributeValue);
	}

	@Test
	public void postConnect() {
		request.setAttribute("twitterConnect.postTweet", Boolean.TRUE, WebRequest.SCOPE_SESSION);
		AccountProvider<TwitterOperations> provider = mock(AccountProvider.class);
		TwitterOperations twitterOperations = mock(TwitterOperations.class);
		when(provider.getApi(2L)).thenReturn(twitterOperations);
		Account account = new Account(2L, "Craig", "Walls", "cwalls@vmware.com", "habuma", "http://picture.com/url",
				new UriTemplate("http://greenhouse.springsource.org/members/{profile}"));
		interceptor.postConnect(provider, account, request);
		verify(twitterOperations).updateStatus(
				"Join me at the Greenhouse! http://greenhouse.springsource.org/members/habuma");
	}

	@Test
	public void postConnect_noPostTweetAttribute() {
		AccountProvider<TwitterOperations> provider = mock(AccountProvider.class);
		interceptor.postConnect(provider, null, request);
		verify(provider, never()).getApi(anyLong());
	}
}
