package org.springframework.mobile;

import net.sourceforge.wurfl.core.Device;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

public class DeviceWebArgumentResolver implements WebArgumentResolver {
	public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
		if (Device.class.isAssignableFrom(param.getParameterType())) {
			return request.getAttribute(DeviceDetectingHandlerInterceptor.CURRENT_DEVICE_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
		} else {
			return WebArgumentResolver.UNRESOLVED;
		}
	}
}