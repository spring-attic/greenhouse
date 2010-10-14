package com.springsource.greenhouse.invite.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

}