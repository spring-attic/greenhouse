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
package com.springsource.greenhouse.invite;

import static org.junit.Assert.*;

import org.junit.Test;

public class InviteeTest {
	
	@Test
	public void valueOf() {
		Invitee invitee = Invitee.valueOf("Keith Donald <keith.donald@springsource.com>");
		assertEquals("Keith", invitee.getFirstName());
		assertEquals("Donald", invitee.getLastName());
		assertEquals("keith.donald@springsource.com", invitee.getEmail());
	}
	
	@Test
	public void emailOnlyNoBrackets() {
		Invitee invitee = Invitee.valueOf("keith.donald@springsource.com");
		assertNull(invitee.getFirstName());
		assertNull(invitee.getLastName());
		assertEquals("keith.donald@springsource.com", invitee.getEmail());
	}

	@Test
	public void emailOnlyBrackets() {
		Invitee invitee = Invitee.valueOf("<keith.donald@springsource.com>");
		assertNull(invitee.getFirstName());
		assertNull(invitee.getLastName());
		assertEquals("keith.donald@springsource.com", invitee.getEmail());
	}

	@Test
	public void firstNameOnly() {
		Invitee invitee = Invitee.valueOf("Keith <keith.donald@springsource.com>");
		assertEquals("Keith", invitee.getFirstName());
		assertNull(invitee.getLastName());
		assertEquals("keith.donald@springsource.com", invitee.getEmail());
	}
	
	@Test
	public void extraWhitespace() {
		Invitee invitee = Invitee.valueOf("Keith Donald    <keith.donald@springsource.com>    ");
		assertEquals("Keith", invitee.getFirstName());
		assertEquals("Donald", invitee.getLastName());
		assertEquals("keith.donald@springsource.com", invitee.getEmail());
	}

}