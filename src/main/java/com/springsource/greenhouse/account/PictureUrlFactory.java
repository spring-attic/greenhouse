package com.springsource.greenhouse.account;

import org.springframework.data.FileStorage;

public class PictureUrlFactory {

	private FileStorage pictureStorage;

	public PictureUrlFactory(FileStorage pictureStorage) {
		this.pictureStorage = pictureStorage;
	}

	public String pictureUrl(Long accountId, PictureSize size, boolean pictureSet, Gender gender) {
		if (size == null) {
			size = PictureSize.normal;
		}
		if (pictureSet) {
			return getProfilePicturePath() + "/" + accountId + "/" + size + ".jpg";
		} else {
			return defaultPictureUrl(gender, size);
		}
	}

	public String defaultPictureUrl(Gender gender, PictureSize size) {
		if (gender == Gender.Male) {
			return malePictureUrl(size);
		} else {
			return femalePictureUrl(size);				
		}
	}
	
	private String malePictureUrl(PictureSize size) {
		return getProfilePicturePath() + "/male/" + size + ".jpg";
	}

	private String femalePictureUrl(PictureSize size) {
		return getProfilePicturePath() + "/female/" + size + ".jpg";
	}

	private String getProfilePicturePath() {
		return pictureStorage.absoluteUrl("profile-pics");
	}
}
