package com.springsource.greenhouse.connect;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookLink;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountUtils;
import com.springsource.greenhouse.members.ProfilePictureService;
import com.springsource.greenhouse.utils.Message;

/**
 * Supports posting to the wall on behalf of the user after connecting to Facebook.
 * Also supports using the user's Facebook profile picture as their local profile picture.
 * @author Keith Donald
 */
public class FacebookConnectInterceptor implements ConnectInterceptor<Facebook> {

	private final ProfilePictureService profilePictureService;

	@Inject
	public FacebookConnectInterceptor(ProfilePictureService profilePictureService) {
		this.profilePictureService = profilePictureService;
	}

	public void preConnect(ConnectionFactory<Facebook> provider, MultiValueMap<String, String> params, WebRequest request) {
		if (StringUtils.hasText(request.getParameter(POST_TO_WALL_PARAMETER))) {
			request.setAttribute(POST_TO_WALL_ATTRIBUTE, Boolean.TRUE, WebRequest.SCOPE_SESSION);
		}
		if (StringUtils.hasText(request.getParameter(USE_FACEBOOK_IMAGE_PARAMETER))) {
			request.setAttribute(USE_FACEBOOK_IMAGE_ATTRIBUTE, Boolean.TRUE, WebRequest.SCOPE_SESSION);
		}
	}

	public void postConnect(Connection<Facebook> connection, WebRequest request) {
		Account account = AccountUtils.getCurrentAccount();
		postToWall(connection, account, request);
		useFacebookProfileImage(connection, account, request);
	}

	// internal helpers
	
	private void postToWall(Connection<Facebook> connection, Account account, WebRequest request) {
		if (request.getAttribute(POST_TO_WALL_ATTRIBUTE, WebRequest.SCOPE_SESSION) != null) {
			connection.getApi().feedOperations().postLink("Join me at the Greenhouse!", new FacebookLink(account.getProfileUrl(), "Greenhouse", "Where Spring developers hang out.",
				"We help you connect with fellow application developers and take advantage of everything the Spring community has to offer."));
			request.removeAttribute(POST_TO_WALL_ATTRIBUTE, WebRequest.SCOPE_SESSION);
		}
	}

	private void useFacebookProfileImage(Connection<Facebook> connection, Account account, WebRequest request) {
		if (request.getAttribute(USE_FACEBOOK_IMAGE_ATTRIBUTE, WebRequest.SCOPE_SESSION) != null) {
			try {
				profilePictureService.saveProfilePicture(account.getId(), connection.getApi().userOperations().getUserProfileImage());
			} catch (IOException e) {
				NativeWebRequest nativeRequest = (NativeWebRequest) request;
				HttpServletRequest servletRequest = nativeRequest.getNativeRequest(HttpServletRequest.class);
				FlashMap flashMap = RequestContextUtils.getOutputFlashMap(servletRequest);
				flashMap.put("message", Message.warning("Greenhouse was unable to use your Facebook profile picture."));
			}
			request.removeAttribute(USE_FACEBOOK_IMAGE_ATTRIBUTE, WebRequest.SCOPE_SESSION);			
		}
	}

	private static final String POST_TO_WALL_PARAMETER = "postToWall";

	private static final String POST_TO_WALL_ATTRIBUTE = "facebookConnect." + POST_TO_WALL_PARAMETER;

	private static final String USE_FACEBOOK_IMAGE_PARAMETER = "useProfilePicture";

	private static final String USE_FACEBOOK_IMAGE_ATTRIBUTE = "facebookConnect." + USE_FACEBOOK_IMAGE_PARAMETER;

}
