package com.springsource.greenhouse.account;

public abstract class ProfileUrlUtils {

	// TODO do not hardcode this
	private static String PROFILE_URL_PATH = "http://localhost:8080/greenhouse/members/";
	
	private static String PROFILE_PICTURE_URL_PATH = "http://images.greenhouse.springsource.org/profile-pics/";
	
	public static String url(Account account) {
		return PROFILE_URL_PATH + account.getProfileKey();
	}
	
	public static String pictureUrl(Long accountId, PictureSize size, boolean pictureSet, Gender gender) {
		if (size == null) {
			size = PictureSize.normal;
		}
		if (pictureSet) {
			return PROFILE_PICTURE_URL_PATH + accountId + "/" + size + ".jpg";
		} else {
			return defaultPictureUrl(gender, size);
		}
	}

	public static String defaultPictureUrl(Gender gender, PictureSize size) {
		if (gender == Gender.Male) {
			return ProfileUrlUtils.malePictureUrl(size);
		} else {
			return ProfileUrlUtils.femalePictureUrl(size);				
		}
	}
	
	public static String malePictureUrl(PictureSize size) {
		return PROFILE_PICTURE_URL_PATH + "male/" + size + ".jpg";
	}

	public static String femalePictureUrl(PictureSize size) {
		return PROFILE_PICTURE_URL_PATH + "female/" + size + ".jpg";
	}

	private ProfileUrlUtils() {		
	}

}
