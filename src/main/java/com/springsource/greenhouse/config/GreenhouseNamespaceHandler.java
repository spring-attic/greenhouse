package com.springsource.greenhouse.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class GreenhouseNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser("environment-bean", new EnvironmentBeanDefinitionParser());
	}

}

