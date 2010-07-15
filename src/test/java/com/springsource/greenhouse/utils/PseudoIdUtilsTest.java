package com.springsource.greenhouse.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class PseudoIdUtilsTest {
	@Test
	public void shouldProducePseudoId() {
		assertEquals("test", PseudoIdUtils.generatePseudoId("test"));
		assertEquals("test_message", PseudoIdUtils.generatePseudoId("test message"));
		assertEquals("test_message_with_more_than", 
				PseudoIdUtils.generatePseudoId("test message with more than five words"));
		assertEquals("test_message_colon_edition", PseudoIdUtils.generatePseudoId("test message: colon edition"));
		assertEquals("test_message_number_5", PseudoIdUtils.generatePseudoId("test message number 5"));
		assertEquals("another_test_messages", PseudoIdUtils.generatePseudoId("another @%$#! test messages"));
	}
}
