package com.springsource.greenhouse.signin;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.account.Account;

public class AccountExposingHandlerInterceptorTest {
	
    private AccountExposingHandlerInterceptor interceptor;
    
    private Account account;
    
    @Before
    public void setup() {
        interceptor = new AccountExposingHandlerInterceptor();
        account = new Account(1L, "Joe", "Schmoe", "joe@schmoe.com", "joe", "file://pic.jpg", new UriTemplate("http://localhost:8080/members/{profileKey}"));
        TestingAuthenticationToken authentication = new TestingAuthenticationToken(account, "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    
    @Test
    public void preHandle() throws Exception {
    	MockHttpServletRequest request = new MockHttpServletRequest();
        interceptor.preHandle(request, null, null);            
        assertSame(account, request.getAttribute("account"));
    }
    
}