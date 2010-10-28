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

import java.util.List;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.ProfileReference;

/**
 * Models the provider of a service that local member accounts may connect to and invoke.
 * Exposes service provider metadata along with connection management operations that allow for account connections to be established.
 * Also acts as a factory for a strongly-typed service API (S).
 * Once a connection with this provider is established, the service API can be used by the application to invoke the service on behalf of the member.
 * @author Keith Donald
 * @param <S> The service API hosted by this service provider.
 */
public interface ServiceProvider<S> {

	// provider meta-data
	
	/**
	 * The unique name or id of the service provider e.g. twitter.
	 * Unique across all service providers.
	 */
	String getName();
	
	/**
	 * A label suitable for display in a UI, typically used to inform the user which service providers he or she has connected with / may connect with. e.g. Twitter.
	 */
	String getDisplayName();

	/**
	 * The key used to identify the local application with the remote service provider.
	 * Used when establishing an account connection with the service provider.
	 * Available as a public property to support client code that wishes to manage the service connection process itself, for example, in JavaScript.
	 * The term "API key" is derived from the OAuth 2 specification. 
	 */
	String getApiKey();
	
	/**
	 * An alternate identifier for the local application in the remote service provider's system.
	 * May be null of no such alternate identifier exists.
	 * Used by ServiceProvider&lt;FacebookOperations&gt; to support "Like" functionality.
	 * @return an alternate app id, or null if no alternate id exists (null is the typical case, as the {@link #getApiKey()} is the primary means of consumer identification)
	 */
	Long getAppId();

	// connection management
	
	/**
	 * Begin the account connection process by fetching a new request token from this service provider.
	 * The returned token should be stored with in the member's session for later use.
	 * @param callbackUrl the URL the provider should redirect to after the member authorizes the connection (may be null for OAuth 1.0-based service providers) 
	 */
	OAuthToken fetchNewRequestToken(String callbackUrl);

	/**
	 * Construct the URL to redirect the member to for service connection authorization.
	 * @param requestToken the request token value, to be encoded in the authorize URL
	 * @return the absolute authorize URL to redirect the member to
	 */
	String buildAuthorizeUrl(String requestToken);

	/**
	 * Connects a member account to this service provider.
	 * Exchanges the request token and verifier for an access token, then stores the granted access token with the account.
	 * @param accountId the member account identifier
	 * @param requestToken the token obtained at the beginning of the connection process
	 * @param verifier the verifier returned in the provider callback following connection authorization (may be null for some providers).
	 */
	void connect(Long accountId, OAuthToken requestToken, String verifier);

	/**
	 * Records an existing connection between a member account and this service provider.
	 * Use when the connection process happens outside of the control of this package; for example, in JavaScript.
	 * @param accountId the member account identifier
	 * @param accessToken the access token that was granted as a result of the connection
	 * @param providerAccountId the id of the user in the provider's system; may be an assigned number or a user-selected screen name.
	 */
	void addConnection(Long accountId, String accessToken, String providerAccountId);

	/**
	 * Returns true if the member account is connected to this provider, false otherwise.
	 */
	boolean isConnected(Long accountId);

	/**
	 * Gets a handle to the API offered by this service provider.
	 * This API may be used by the application to invoke the service on behalf of a member.
	 * @param accountId the member account identifier (may be null, if so, only operations that require no authorization may be invoked) 
	 */
	S getServiceOperations(Long accountId);

	/**
	 * Sever the connection between the member account and this service provider.
	 * Has no effect if no connection is established to begin with.
	 */
	void disconnect(Long accountId);
	
	// additional finders

	/**
	 * The id of the member in the provider's system.
	 * May be an assigned internal identifier, such as a sequence number, or a user-selected screen name.
	 * Generally unique across accounts registered with this provider.
	 */
	String getProviderAccountId(Long accountId);

	/**
	 * Authenticate a member Account by a connection established with this service provider.
	 * Used to support "Sign in using Facebook"-type scenarios, where the access token identifying a connection is available to client code, typically a cookie managed by JavaScript.
	 * @throws NoSuchAccountConnectionException no such connection has been established between a member and this service provider
	 */
	Account findAccountByConnection(String accessToken) throws NoSuchAccountConnectionException;
	
	/**
	 * Find the members connected to this provider that have the specified account ids in the provider's system.
	 * Used to see which of your friends in the provider's network also have member accounts with the local application.
	 */
	List<ProfileReference> findMembersConnectedTo(List<String> providerAccountIds);

}