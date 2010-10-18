package com.springsource.greenhouse.action;

import org.springframework.integration.annotation.Gateway;

public interface ActionGateway {
	
	@Gateway
	void actionPerformed(Action action);
	
}
