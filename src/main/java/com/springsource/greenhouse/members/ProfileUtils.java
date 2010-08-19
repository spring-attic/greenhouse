package com.springsource.greenhouse.members;

public abstract class ProfileUtils {

	private static String PROFILE_PIC_URL_PATH = "http://images.greenhouse.springsource.org/profile-pics/";
	
	public static String picUrl(Long accountId, PictureSize size, boolean pictureSet, char gender) {
		if (size == null) {
			size = PictureSize.normal;
		}
		if (pictureSet) {
			return PROFILE_PIC_URL_PATH + accountId + "/" + size + ".jpg";
		} else {
			if (gender == 'M') {
				return PROFILE_PIC_URL_PATH + "male/" + size + ".jpg";
			} else {
				return PROFILE_PIC_URL_PATH + "female/" + size + ".jpg";			
			}
		}
	}
	
	private ProfileUtils() {		
	}

}
