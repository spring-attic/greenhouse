package com.springsource.greenhouse.members;

public abstract class ProfileUtils {

	public static String picUrl(Long accountId, String type) {
		return "http://images.greenhouse.springsource.org/profile-pics/" + accountId + "/" + (type != null ? type : "normal") + ".jpg";
	}
	
	private ProfileUtils() {		
	}

}
