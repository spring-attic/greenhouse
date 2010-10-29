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

import com.springsource.greenhouse.account.AccountException;

/**
 * The key submitted by the client application to identify itself with the server is invalid.
 * Will be thrown if the app has been deleted or a developer reset the keys but forgot to update the client code.
 * @author Keith Donald
 */
@SuppressWarnings("serial")
public class InvalidApiKeyException extends AccountException {

	private String apiKey;
	
	public InvalidApiKeyException(String apiKey) {
		super("invalid api key " + apiKey);
		this.apiKey = apiKey;
	}
	
	public String getApiKey() {
		return apiKey;
	}

}
