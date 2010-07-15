package com.springsource.greenhouse.utils;

import java.util.Arrays;

import org.springframework.util.StringUtils;


public class FriendlyIdUtils {
	public static String generateFriendlyId(String title) {		
		String[] split = title.toLowerCase().split("[\\W\\s]+");
		if(split.length > 5) {
			split = Arrays.copyOf(split, 5);
		}
		return StringUtils.collectionToDelimitedString(Arrays.asList(split), "_");
	}
}
