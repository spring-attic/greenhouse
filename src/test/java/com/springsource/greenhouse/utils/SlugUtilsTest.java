package com.springsource.greenhouse.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class SlugUtilsTest {
	
	@Test
	public void toSlug() {
		assertEquals("test", SlugUtils.toSlug("test"));
		assertEquals("test-message", SlugUtils.toSlug("test message"));
		assertEquals("test-message-with-more-than-five-words", 
				SlugUtils.toSlug("test message with more than five words"));
		assertEquals("test-message-colon-edition", SlugUtils.toSlug("test message: colon edition"));
		assertEquals("test-message-number-5", SlugUtils.toSlug("test message number 5"));
		assertEquals("another--test-messages", SlugUtils.toSlug("another @%$#! test messages"));
		assertEquals("this-one-was-in-caps", SlugUtils.toSlug("This ONE wAs In CaPs"));
	}
}
