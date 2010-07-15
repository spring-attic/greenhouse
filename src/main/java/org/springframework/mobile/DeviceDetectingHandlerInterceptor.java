package org.springframework.mobile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.wurfl.core.Device;
import net.sourceforge.wurfl.core.WURFLManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class DeviceDetectingHandlerInterceptor implements HandlerInterceptor {

	public static final String DEVICE_ATTRIBUTE = "wurflDevice";
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private WURFLManager wurflManager;

	public DeviceDetectingHandlerInterceptor(WURFLManager wurflManager) {
		this.wurflManager = wurflManager;
	}

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String userAgent = request.getHeader("User-Agent");
		Device device = wurflManager.getDeviceForRequest(userAgent);
		Device defaultDevice = new DefaultDevice(device);
		
		if (device != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Device: " + device.getId());
				logger.debug("Mobile Browser: " + device.getCapability("mobile_browser"));
			}
			request.setAttribute(DEVICE_ATTRIBUTE, defaultDevice);
		}
		return true;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {	
	}
}
