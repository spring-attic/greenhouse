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
 * Strategy for storing account connection information.
 * Delegated to {@link AbstractServiceProvider} to decouple the provider implementation from any physical connection store.
 * @author Keith Donald
 */
public interface AccountConnectionRepository {

	void addConnection(Long accountId, String provider, OAuthToken accessToken, String providerAccountId, String providerProfileUrl);

	boolean isConnected(Long accountId, String provider);

	void disconnect(Long accountId, String provider);

	OAuthToken getAccessToken(Long accountId, String provider);

	String getProviderAccountId(Long accountId, String provider);

	Account findAccountByConnection(String provider, String accessToken) throws NoSuchAccountConnectionException;

	List<ProfileReference> findMembersConnectedTo(String provider, List<String> providerAccountIds);

}