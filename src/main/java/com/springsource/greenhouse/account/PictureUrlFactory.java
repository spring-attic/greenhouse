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
package com.springsource.greenhouse.account;

import org.springframework.data.FileStorage;

/**
 * Used by Account mappers to derive absolute profile picture URLs from low-level Account fields. 
 * @author Keith Donald
 */
public class PictureUrlFactory {

	private final FileStorage pictureStorage;

	/**
	 * Creates a PictureUrlFactory that produces picture URLs that access pics from the FileStorage provided.
	 */
	public PictureUrlFactory(FileStorage pictureStorage) {
		this.pictureStorage = pictureStorage;
	}

	/**
	 * An absolute member profile picture URL.
	 * @param accountId the member account id
	 * @param size the desired picture size
	 * @param pictureSet a flag indicating if the member has set their profile picture yet; if false a default profile picture URL will be returned
	 * @param gender the gender of the member
	 */
	public String pictureUrl(Long accountId, PictureSize size, boolean pictureSet, Gender gender) {
		if (size == null) {
			size = PictureSize.NORMAL;
		}
		if (pictureSet) {
			return getProfilePicturePath() + "/" + accountId + "/" + filename(size);
		} else {
			return defaultPictureUrl(gender, size);
		}
	}

	/**
	 * The gender's default profile picture URL for the desired size. 
	 */
	public String defaultPictureUrl(Gender gender, PictureSize size) {
		if (gender == Gender.MALE) {
			return malePictureUrl(size);
		} else {
			return femalePictureUrl(size);				
		}
	}
	
	// internal helpers
	
	private String malePictureUrl(PictureSize size) {
		return getProfilePicturePath() + "/male/" + filename(size);
	}

	private String femalePictureUrl(PictureSize size) {
		return getProfilePicturePath() + "/female/" + filename(size);
	}

	private String getProfilePicturePath() {
		return pictureStorage.absoluteUrl("profile-pics");
	}
	
	private String filename(PictureSize size) {
		return size.name().toLowerCase() + ".jpg";
	}
}
