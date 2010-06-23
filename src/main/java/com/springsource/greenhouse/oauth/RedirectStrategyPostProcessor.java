package com.springsource.greenhouse.oauth;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.oauth.provider.UserAuthorizationSuccessfulAuthenticationHandler;

public class RedirectStrategyPostProcessor implements BeanPostProcessor {

	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		if (bean instanceof UserAuthorizationSuccessfulAuthenticationHandler) { 
			UserAuthorizationSuccessfulAuthenticationHandler handler = (UserAuthorizationSuccessfulAuthenticationHandler) bean;
			handler.setRedirectStrategy(new BasicRedirectStrategy());
		}
		return bean;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

}
