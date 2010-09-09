package com.springsource.greenhouse.utils;

import java.util.regex.Pattern;

public final class EmailUtils {
	
	public static boolean isEmail(String email) {
		return emailPattern.matcher(email).matches();
	}
	
	private static final Pattern emailPattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
	
	private EmailUtils() {
		
	}

}
