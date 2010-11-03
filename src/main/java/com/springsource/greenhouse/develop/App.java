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
package com.springsource.greenhouse.develop;

/**
 * Details about a client application that can invoke the API of the server application on behalf of a member.
 * @author Keith Donald
 */
public class App {
	
	private final AppSummary summary;

	private final String apiKey;
	
	private final String secret;
	
	private final String callbackUrl;

	public App(AppSummary summary, String apiKey, String secret, String callbackUrl) {
		this.summary = summary;
		this.apiKey = apiKey;
		this.secret = secret;
		this.callbackUrl = callbackUrl;
	}

	/**
	 * Short summary of the client application.
	 */
	public AppSummary getSummary() {
		return summary;
	}
	
	/**
	 * The assigned key that identifies the client app and allows it to use the server API.
	 * Must be presented by the client app when establishing a connection.
	 * Should only be known to the application's development team and not shared with others.
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * The assigned secret used for signature verification between the client and server.
	 * Only used for OAuth 1.0-based client applications.
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * The client URL the server should redirect a member after he or she authorizes a connection between the client app and his or her profile.
	 * May be null; if so, it is expected the client app will provide the callbackURL as a parameter during connection handshake.
	 */
	public String getCallbackUrl() {
		return callbackUrl;
	}

}
