package com.springsource.greenhouse.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class FriendlyIdUtilsTest {
	@Test
	public void shouldProducePseudoId() {
		assertEquals("test", FriendlyIdUtils.generateFriendlyId("test"));
		assertEquals("test_message", FriendlyIdUtils.generateFriendlyId("test message"));
		assertEquals("test_message_with_more_than", 
				FriendlyIdUtils.generateFriendlyId("test message with more than five words"));
		assertEquals("test_message_colon_edition", FriendlyIdUtils.generateFriendlyId("test message: colon edition"));
		assertEquals("test_message_number_5", FriendlyIdUtils.generateFriendlyId("test message number 5"));
		assertEquals("another_test_messages", FriendlyIdUtils.generateFriendlyId("another @%$#! test messages"));
		assertEquals("this_one_was_in_caps", FriendlyIdUtils.generateFriendlyId("This ONE wAs In CaPs"));
	}
}
