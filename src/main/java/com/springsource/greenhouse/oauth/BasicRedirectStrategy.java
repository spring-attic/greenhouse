package com.springsource.greenhouse.oauth;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.RedirectStrategy;

public class BasicRedirectStrategy implements RedirectStrategy {

	private static final Logger logger = LoggerFactory.getLogger(BasicRedirectStrategy.class);

	private boolean contextRelative;

	/**
	 * If <tt>true</tt>, causes any redirection URLs to be calculated minus the
	 * protocol and context path (defaults to <tt>false</tt>).
	 */
	public void setContextRelative(boolean useRelativeContext) {
		this.contextRelative = useRelativeContext;
	}

	/**
	 * Redirects the response to the supplied URL.
	 * <p>
	 * If <tt>contextRelative</tt> is set, the redirect value will be the value
	 * after the request context path. Note that this will result in the loss of
	 * protocol information (HTTP or HTTPS), so will cause problems if a
	 * redirect is being performed to change to HTTPS, for example.
	 */
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		String redirectUrl = calculateRedirectUrl(request.getContextPath(), url);
		redirectUrl = response.encodeRedirectURL(redirectUrl);
		if (logger.isDebugEnabled()) {
			logger.debug("Redirecting to '" + redirectUrl + "'");
		}
		response.sendRedirect(redirectUrl);
	}

	private String calculateRedirectUrl(String contextPath, String url) {
		// DUMB HARDCODED APPROACH FOR NOW!!!
		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			if (contextRelative || url.startsWith("x-com-springsource-greenhouse://")) {
				return url;
			} else {
				return contextPath + url;
			}
		}

		// Full URL, including http(s)://

		if (!contextRelative) {
			return url;
		}

		// Calculate the relative URL from the fully qualifed URL, minus the
		// protocol and base context.
		url = url.substring(url.indexOf("://") + 3); // strip off protocol
		url = url.substring(url.indexOf(contextPath) + contextPath.length());

		if (url.length() > 1 && url.charAt(0) == '/') {
			url = url.substring(1);
		}
		return url;
	}

}