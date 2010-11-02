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
package com.springsource.greenhouse.invite;

/**
 * Thrown when an invite could not be found because one indexed by the provided token does not exist.
 * @author Keith Donald
 */
@SuppressWarnings("serial")
public final class NoSuchInviteException extends InviteException {

	public NoSuchInviteException(String token) {
		super(token, "No such invite with token '" + token + "'");
	}

}
