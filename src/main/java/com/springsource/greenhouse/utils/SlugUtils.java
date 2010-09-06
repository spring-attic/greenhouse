package com.springsource.greenhouse.utils;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Pattern;

public class SlugUtils {
	
	  private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
	  
	  private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

	  public static String toSlug(String input) {
	    String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
	    String normalized = Normalizer.normalize(nowhitespace, Form.NFD);
	    String slug = NONLATIN.matcher(normalized).replaceAll("");
	    return slug.toLowerCase(Locale.ENGLISH);
	  }

}
