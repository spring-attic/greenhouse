package org.springframework.security.signin;

public interface SigninService {
	
	void signin(String username, String password, Object requestDetails);
	
}
