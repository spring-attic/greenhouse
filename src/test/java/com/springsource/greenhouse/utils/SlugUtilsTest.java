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
