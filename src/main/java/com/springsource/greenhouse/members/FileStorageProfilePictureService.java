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
package com.springsource.greenhouse.members;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.FileData;
import org.springframework.data.FileStorage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.springsource.greenhouse.account.AccountMapper;
import com.springsource.greenhouse.utils.ImageUtils;

/**
 * PictureProfileService that puts profile pics into FileStorage.
 * Also sets a flag associated with the member's account when a profile pic is set.
 * This flag is used when constructing the pictureUrl when {@link AccountMapper mapping} Account and Profile objects. 
 * @author Keith Donald
 * @author Craig Walls
 */
@Service
public class FileStorageProfilePictureService implements ProfilePictureService {

	private final FileStorage storage;

	private final JdbcTemplate jdbcTemplate;
	
	/**
	 * Construct a FileStorageProfilePictureService
	 * @param storage the file storage implementation; for example, to store files in S3 or a local directory.
	 * @param jdbcTemplate the data access template used to update the pictureSet flag
	 */
	@Inject
	public FileStorageProfilePictureService(FileStorage storage, JdbcTemplate jdbcTemplate) {
		this.storage = storage;
		this.jdbcTemplate = jdbcTemplate;
	}

	public void saveProfilePicture(Long accountId, byte[] imageBytes) throws IOException {
		assertBytesLength(imageBytes);
		String contentType = guessContentType(imageBytes);
		assertContentType(contentType);
		storage.storeFile(new FileData("profile-pics/" + accountId + "/small.jpg", ImageUtils.scaleImageToWidth(imageBytes, 50), contentType));
		storage.storeFile(new FileData("profile-pics/" + accountId + "/normal.jpg", ImageUtils.scaleImageToWidth(imageBytes, 100), contentType));
		storage.storeFile(new FileData("profile-pics/" + accountId + "/large.jpg", ImageUtils.scaleImageToWidth(imageBytes, 200), contentType));
		jdbcTemplate.update("update Member set pictureSet = true where id = ?", accountId);		
	}

	// internal helpers

	private void assertBytesLength(byte[] imageBytes) {
		if (imageBytes.length == 0) {
			throw new IllegalArgumentException("Cannot accept empty picture byte[] as a profile picture");
		}
	}

	private String guessContentType(byte[] imageBytes) throws IOException {
		return URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageBytes));
	}

	private void assertContentType(String contentType) {
		if (!IMAGE_TYPES.contains(contentType)) {
			throw new IllegalArgumentException("Cannot accept content type '" + contentType + "' as a profile picture.");
		}
	}

	private static final List<String> IMAGE_TYPES = Arrays.asList("image/jpeg", "image/gif", "image/png");
	
}