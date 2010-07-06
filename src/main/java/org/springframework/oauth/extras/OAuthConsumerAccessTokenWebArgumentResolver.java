package org.springframework.oauth.extras;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth.consumer.token.OAuthConsumerTokenServicesFactory;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

public class OAuthConsumerAccessTokenWebArgumentResolver implements WebArgumentResolver {

	private OAuthConsumerTokenServicesHelper oauthHelper;
	
	public OAuthConsumerAccessTokenWebArgumentResolver(OAuthConsumerTokenServicesFactory oauthTokenFactory) {
		this.oauthHelper = new OAuthConsumerTokenServicesHelper(oauthTokenFactory);
	}

	public Object resolveArgument(MethodParameter parameter, NativeWebRequest request) throws Exception {
		OAuthAccessToken token = parameter.getParameterAnnotation(OAuthAccessToken.class);
		if (token != null) {
			HttpServletRequest nativeRequest = (HttpServletRequest) request.getNativeRequest();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			return oauthHelper.getAccessToken(token.value(), nativeRequest, auth);
		} else {
			return WebArgumentResolver.UNRESOLVED;
		}
	}

}
