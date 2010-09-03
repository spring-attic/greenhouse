package com.springsource.greenhouse;

import org.springframework.stereotype.Component;

@Component("facebookApiProvider")
public class ApiProvider {
	public String getApiKey() {
		return "21aa96c8bc23259d0dd2ab99e496c306";
	}
}
