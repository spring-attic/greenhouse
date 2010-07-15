package com.springsource.greenhouse.utils;

import java.util.Arrays;

import org.springframework.util.StringUtils;


public class PseudoIdUtils {
	public static String generatePseudoId(String title) {		
		String[] split = title.split("[\\W\\s]+");
		if(split.length > 5) {
			split = Arrays.copyOf(split, 5);
		}
		return StringUtils.collectionToDelimitedString(Arrays.asList(split), "_");
	}
}
