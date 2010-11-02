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
package com.springsource.greenhouse.utils;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utilities for generating slugs: a short, meaningful name for a resource that can be used in the resource's friendly URL path.
 * @author Keith Donald
 */
public class SlugUtils {
	
	  private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
	  
	  private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

	  /**
	   * Convert the String input to a slug.
	   */
	  public static String toSlug(String input) {
		if (input == null) {
			throw new IllegalArgumentException("Input cannot be null");
		}
	    String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
	    String normalized = Normalizer.normalize(nowhitespace, Form.NFD);
	    String slug = NONLATIN.matcher(normalized).replaceAll("");
	    return slug.toLowerCase(Locale.ENGLISH);
	  }

}
