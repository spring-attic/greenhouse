package com.springsource.greenhouse.connect.providers;

import java.io.IOException;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.social.connect.ServiceProvider;
import org.springframework.social.connect.ServiceProviderConnection;
import org.springframework.social.facebook.FacebookApi;
import org.springframework.social.facebook.FacebookLink;
import org.springframework.social.twitter.DuplicateTweetException;
import org.springframework.social.web.connect.ConnectInterceptor;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.members.ProfilePictureService;

public class FacebookConnectInterceptor implements ConnectInterceptor<FacebookApi> {

	private final ProfilePictureService profilePictureService;

	private final RestTemplate restTemplate;

	@Inject
	public FacebookConnectInterceptor(ProfilePictureService profilePictureService) {
		this.profilePictureService = profilePictureService;
		restTemplate = new RestTemplate();
		// the default connection factory doesn't follow redirects, which the image request returns
		restTemplate.setRequestFactory(new CommonsClientHttpRequestFactory());
	}

	@Override
	public void preConnect(ServiceProvider<FacebookApi> provider, WebRequest request) {
		if (StringUtils.hasText(request.getParameter(POST_TO_WALL_PARAMETER))) {
			request.setAttribute(POST_TO_WALL_ATTRIBUTE, Boolean.TRUE, WebRequest.SCOPE_SESSION);
		}
		if (StringUtils.hasText(request.getParameter(USE_FACEBOOK_IMAGE_PARAMETER))) {
			request.setAttribute(USE_FACEBOOK_IMAGE_ATTRIBUTE, Boolean.TRUE, WebRequest.SCOPE_SESSION);
		}
	}

	@Override
	public void postConnect(ServiceProvider<FacebookApi> provider, ServiceProviderConnection<FacebookApi> connection,
			WebRequest request) {

		// relies on AccountExposingHandlerInterceptor to have put the account in the request.
		Account account = (Account) request.getAttribute("account", WebRequest.SCOPE_REQUEST);

		FacebookApi facebook = connection.getServiceApi();
		postToWall(facebook, account, request);
		useFacebookProfileImage(facebook, account, request);
	}

	private void postToWall(FacebookApi facebook, Account account, WebRequest request) {
		if (request.getAttribute(POST_TO_WALL_ATTRIBUTE, WebRequest.SCOPE_SESSION) != null) {
			try {
				facebook.updateStatus("Join me at the Greenhouse!",new FacebookLink(account.getProfileUrl(), "Greenhouse","Where Spring developers hang out.",
					"We help you connect with fellow application developers and take advantage of everything the Spring community has to offer."));
			} catch (DuplicateTweetException e) {
			}
			request.removeAttribute(POST_TO_WALL_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		}
	}

	private void useFacebookProfileImage(FacebookApi facebook, Account account, WebRequest request) {
		if (request.getAttribute(USE_FACEBOOK_IMAGE_ATTRIBUTE, WebRequest.SCOPE_SESSION) != null) {
			try {
				profilePictureService.saveProfilePicture(account.getId(), getProfilePicture(facebook.getProfileId()));
			} catch (IOException e) {
				FlashMap.setWarningMessage("Greenhouse was unable to use your Facebook profile picture.");
			}
		}
	}

	private byte[] getProfilePicture(String profileId) {
		ResponseEntity<byte[]> imageBytes = restTemplate.getForEntity(PROFILE_LARGE_PICTURE_URL, byte[].class, profileId);
		return imageBytes.getBody();
	}


	private static final String POST_TO_WALL_PARAMETER = "postToWall";

	private static final String POST_TO_WALL_ATTRIBUTE = "facebookConnect." + POST_TO_WALL_PARAMETER;

	private static final String USE_FACEBOOK_IMAGE_PARAMETER = "useProfilePicture";

	private static final String USE_FACEBOOK_IMAGE_ATTRIBUTE = "facebookConnect." + USE_FACEBOOK_IMAGE_PARAMETER;

	private static final String PROFILE_LARGE_PICTURE_URL = "https://graph.facebook.com/{profileId}/picture?type=large";

}
