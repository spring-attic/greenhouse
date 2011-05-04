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
 * Thrown if an invite has already been accepted and you try to accept it again.
 * You can only accept an invite once.
 * @author Keith Donald
 */
@SuppressWarnings("serial")
public final class InviteAlreadyAcceptedException extends InviteException {
	
	public InviteAlreadyAcceptedException(String token) {
		super(token, "Invite already accepted with token '" + token + "'");
	}
	
}
