/*
 * Copyright 2010 the original author or authors.
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
package com.springsource.greenhouse.connect;

import org.springframework.web.context.request.WebRequest;

import com.springsource.greenhouse.account.Account;

/**
 * Listens for service provider connection events.
 * Allows for custom logic to be executed before and after connections are established with a specific service provider.
 * @author Keith Donald
 * @param <S> The service API hosted by the intercepted service provider.
 */
public interface ConnectInterceptor<S> {
	
	/**
	 * Called during connection initiation, immediately before user authorization.
	 * Used to store custom connection attributes in the session before redirecting the user to the provider's site.
	 */
	void preConnect(ServiceProvider<S> provider, WebRequest request);

	/**
	 * Called immediately after the connection is established.
	 * Used to invoke the service API on behalf of the user upon connecting.
	 */
	void postConnect(ServiceProvider<S> provider, Account account, WebRequest request);
	
}
