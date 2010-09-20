package com.springsource.greenhouse.signin;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.signin.AccountExposingHandlerInterceptor;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriTemplate;

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
    public void preHandleShouldDoNothingInteresting() throws Exception {
        assertTrue(interceptor.preHandle(null, null, null));
    }
    
    @Test
    public void postHandleShouldAddCurrentUserToModel() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        interceptor.postHandle(null, null, null, modelAndView);            
        assertSame(account, modelAndView.getModelMap().get("account"));
    }
    
    @Test
    public void postHandleShouldNotAddCurrentUserToModelIfModelAndViewIsNull() throws Exception {
        interceptor.postHandle(null, null, null, null);            
    }
    
    @Test
    public void postHandleShouldNotAddCurrentUserToModelIfAuthenticationIsNotInSecurityContext() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        SecurityContextHolder.getContext().setAuthentication(null);
        interceptor.postHandle(null, null, null, modelAndView);  
        assertNull(modelAndView.getModelMap().get("account"));
    }
    
    @Test
    public void postHandleShouldNotAddCurrentUserToModelIfAuthenticationIsInstanceOfAnonymousAuthenticationToken() throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        List<GrantedAuthority> anonymousAuthorities = Arrays.asList(new GrantedAuthority[] {new GrantedAuthorityImpl("ROLE_ANONYMOUS")});
        SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("key", "principal", anonymousAuthorities));
        interceptor.postHandle(null, null, null, modelAndView);  
        assertNull(modelAndView.getModelMap().get("account"));
    }
    
    @Test
    public void shouldDoNothingAfterCompletion() throws Exception {
        interceptor.afterCompletion(null, null, null, null);
    }
}