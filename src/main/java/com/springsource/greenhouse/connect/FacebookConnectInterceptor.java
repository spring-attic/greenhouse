package com.springsource.greenhouse.connect;

import java.io.IOException;

import javax.inject.Inject;

import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.social.connect.ServiceProviderConnection;
import org.springframework.social.connect.ServiceProviderConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.facebook.api.FacebookApi;
import org.springframework.social.facebook.api.FacebookLink;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.flash.FlashMap;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountUtils;
import com.springsource.greenhouse.members.ProfilePictureService;

/**
 * Supports posting to the wall on behalf of the user after connecting to Facebook.
 * Also supports using the user's Facebook profile picture as their local profile picture.
 * @author Keith Donald
 */
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

	public void preConnect(ServiceProviderConnectionFactory<FacebookApi> provider, WebRequest request) {
		if (StringUtils.hasText(request.getParameter(POST_TO_WALL_PARAMETER))) {
			request.setAttribute(POST_TO_WALL_ATTRIBUTE, Boolean.TRUE, WebRequest.SCOPE_SESSION);
		}
		if (StringUtils.hasText(request.getParameter(USE_FACEBOOK_IMAGE_PARAMETER))) {
			request.setAttribute(USE_FACEBOOK_IMAGE_ATTRIBUTE, Boolean.TRUE, WebRequest.SCOPE_SESSION);
		}
	}

	public void postConnect(ServiceProviderConnection<FacebookApi> connection, WebRequest request) {
		FacebookApi facebook = connection.getServiceApi();
		Account account = AccountUtils.getCurrentAccount();
		postToWall(facebook, account, request);
		useFacebookProfileImage(connection, account, request);
	}

	// internal helpers
	
	private void postToWall(FacebookApi facebook, Account account, WebRequest request) {
		if (request.getAttribute(POST_TO_WALL_ATTRIBUTE, WebRequest.SCOPE_SESSION) != null) {
			facebook.feedOperations().postLink("Join me at the Greenhouse!", new FacebookLink(account.getProfileUrl(), "Greenhouse", "Where Spring developers hang out.",
				"We help you connect with fellow application developers and take advantage of everything the Spring community has to offer."));
			request.removeAttribute(POST_TO_WALL_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		}
	}

	private void useFacebookProfileImage(ServiceProviderConnection<FacebookApi> facebook, Account account, WebRequest request) {
		if (request.getAttribute(USE_FACEBOOK_IMAGE_ATTRIBUTE, WebRequest.SCOPE_SESSION) != null) {
			try {
				profilePictureService.saveProfilePicture(account.getId(), getProfilePicture(facebook.getKey().getProviderUserId()));
			} catch (IOException e) {
				FlashMap.setWarningMessage("Greenhouse was unable to use your Facebook profile picture.");
			}
			request.removeAttribute(USE_FACEBOOK_IMAGE_ATTRIBUTE, WebRequest.SCOPE_SESSION);			
		}
	}

	private byte[] getProfilePicture(String profileId) {
		return restTemplate.getForEntity(PROFILE_LARGE_PICTURE_URL, byte[].class, profileId).getBody();
	}

	private static final String POST_TO_WALL_PARAMETER = "postToWall";

	private static final String POST_TO_WALL_ATTRIBUTE = "facebookConnect." + POST_TO_WALL_PARAMETER;

	private static final String USE_FACEBOOK_IMAGE_PARAMETER = "useProfilePicture";

	private static final String USE_FACEBOOK_IMAGE_ATTRIBUTE = "facebookConnect." + USE_FACEBOOK_IMAGE_PARAMETER;

	private static final String PROFILE_LARGE_PICTURE_URL = "https://graph.facebook.com/{profileId}/picture?type=large";

}
