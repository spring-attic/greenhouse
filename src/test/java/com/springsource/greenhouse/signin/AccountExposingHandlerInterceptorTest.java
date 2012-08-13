/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.signin;

import static org.junit.Assert.*;

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