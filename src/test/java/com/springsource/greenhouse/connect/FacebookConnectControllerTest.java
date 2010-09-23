package com.springsource.greenhouse.connect;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.social.facebook.FacebookLink;
import org.springframework.social.facebook.FacebookOperations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.members.ProfilePictureService;

public class FacebookConnectControllerTest {
	private FacebookConnectController controller;
	private FacebookAccountProvider accountProvider;
	private ProfilePictureService profilePictureService;
	private AccountRepository accountRepository;
	private FacebookOperations facebook;

	@Before
	public void setup() {
		accountRepository = mock(AccountRepository.class);
		accountProvider = mock(FacebookAccountProvider.class);
		when(accountProvider.isConnected(1L)).thenReturn(true);
		when(accountProvider.isConnected(2L)).thenReturn(false);
		facebook = mock(FacebookOperations.class);
		when(facebook.getProfilePicture(eq("zeke"))).thenReturn("test image".getBytes());
		when(accountProvider.getFacebookApi(2L)).thenReturn(facebook);

		HttpServletRequest request = new MockHttpServletRequest();
		RequestAttributes attributes = new ServletWebRequest(request);
		RequestContextHolder.setRequestAttributes(attributes);

		profilePictureService = mock(ProfilePictureService.class);

		controller = new FacebookConnectController(accountRepository, accountProvider,
				profilePictureService);
	}

	@Test
	public void connectView() {
		Account connectedAccount = new Account(1L, "Adam", "Anderson", "adam@anderson.com", "adaman", "", null);
		Account disConnectedAccount = new Account(2L, "Zeke", "Zamora", "zeke@zamora.com", "zeke", "", null);
		assertEquals("connect/facebookConnected", controller.connectView(connectedAccount, "adaman", new ExtendedModelMap()));
		assertEquals("connect/facebookConnect", controller.connectView(disConnectedAccount, "zeke", new ExtendedModelMap()));
	}

	@Test
	public void connectAccountToFacebook_noUserIdOrToken() throws Exception {
		Account accountToConnect = new Account(2L, "Zeke", "Zamora", "zeke@zamora.com", "zeke", "", null);
		String view = controller.connectAccountToFacebook(accountToConnect, null, null, false, false);
		verify(accountProvider, never()).connect(any(Long.class), any(ConnectionDetails.class));
		verify(profilePictureService, never()).saveProfilePicture(any(Long.class), any(byte[].class));
		verify(accountRepository, never()).markProfilePictureSet(any(Long.class));
		assertEquals("redirect:/connect/facebook", view);
	}

	@Test
	public void connectAccountToFacebook() throws Exception {
		Account accountToConnect = new Account(2L, "Zeke", "Zamora", "zeke@zamora.com", "zeke", "", null);
		String view = controller.connectAccountToFacebook(accountToConnect, "access_token", "zeke", false, false);
		verify(accountProvider, times(1)).connect(any(Long.class), any(ConnectionDetails.class));
		verify(profilePictureService, never()).saveProfilePicture(any(Long.class), any(byte[].class));
		verify(accountRepository, never()).markProfilePictureSet(any(Long.class));
		assertEquals("redirect:/connect/facebook", view);
	}

	@Test
	public void connectAccountToFacebook_useFacebookProfilePicture() throws Exception {
		Account accountToConnect = new Account(2L, "Zeke", "Zamora", "zeke@zamora.com", "zeke", "", null);
		String view = controller.connectAccountToFacebook(accountToConnect, "access_token", "zeke", false, true);
		verify(accountProvider, times(1)).connect(any(Long.class), any(ConnectionDetails.class));
		verify(profilePictureService, times(1)).saveProfilePicture(any(Long.class), any(byte[].class));
		verify(accountRepository, times(1)).markProfilePictureSet(any(Long.class));
		assertEquals("redirect:/connect/facebook", view);
	}

	@Test
	public void connectAccountToFacebook_postUpdate() throws Exception {
		Account accountToConnect = new Account(2L, "Zeke", "Zamora", "zeke@zamora.com", "zeke", "", new UriTemplate(
				"http://greenhouse/members/{id}"));
		String view = controller.connectAccountToFacebook(accountToConnect, "access_token", "zeke", true, false);
		verify(accountProvider, times(1)).connect(any(Long.class), any(ConnectionDetails.class));
		verify(profilePictureService, never()).saveProfilePicture(any(Long.class), any(byte[].class));
		verify(accountRepository, never()).markProfilePictureSet(any(Long.class));
		verify(facebook, times(1)).postToWall(any(String.class), any(FacebookLink.class));
		assertEquals("redirect:/connect/facebook", view);
	}

}
