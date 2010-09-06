package com.springsource.greenhouse.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class FriendlyIdUtilsTest {
	@Test
	public void shouldProducePseudoId() {
		assertEquals("test", SlugUtils.generateFriendlyId("test"));
		assertEquals("test_message", SlugUtils.generateFriendlyId("test message"));
		assertEquals("test_message_with_more_than", 
				SlugUtils.generateFriendlyId("test message with more than five words"));
		assertEquals("test_message_colon_edition", SlugUtils.generateFriendlyId("test message: colon edition"));
		assertEquals("test_message_number_5", SlugUtils.generateFriendlyId("test message number 5"));
		assertEquals("another_test_messages", SlugUtils.generateFriendlyId("another @%$#! test messages"));
		assertEquals("this_one_was_in_caps", SlugUtils.generateFriendlyId("This ONE wAs In CaPs"));
	}
}
