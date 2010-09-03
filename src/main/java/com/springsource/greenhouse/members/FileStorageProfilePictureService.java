package com.springsource.greenhouse.members;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.data.FileData;
import org.springframework.data.FileStorage;
import org.springframework.stereotype.Service;

import com.springsource.greenhouse.utils.ImageUtils;

@Service
public class FileStorageProfilePictureService implements ProfilePictureService {

	private final FileStorage storage;

	@Inject
	public FileStorageProfilePictureService(FileStorage storage) {
		this.storage = storage;
	}

	public void saveProfilePicture(Long accountId, byte[] imageBytes) throws IOException {
		saveProfilePicture(accountId, imageBytes, guessContentType(imageBytes));
	}

	public void saveProfilePicture(Long accountId, byte[] imageBytes, String contentType) throws IOException {
		assertContentType(contentType);
		assertBytesLength(imageBytes);
		storage.storeFile(new FileData("profile-pics/" + accountId + "/normal.jpg", ImageUtils.scaleImageToWidth(imageBytes, 100), contentType));
		storage.storeFile(new FileData("profile-pics/" + accountId + "/small.jpg", ImageUtils.scaleImageToWidth(imageBytes, 50), contentType));
		storage.storeFile(new FileData("profile-pics/" + accountId + "/large.jpg", ImageUtils.scaleImageToWidth(imageBytes, 200), contentType));
	}

	private static final List<String> IMAGE_TYPES = Arrays.asList("image/jpeg", "image/gif", "image/png");

	private void assertContentType(String contentType) {
		if (!IMAGE_TYPES.contains(contentType)) {
			throw new IllegalArgumentException("Cannot accept content type '" + contentType + "' as a profile picture.");
		}
	}
	
	private void assertBytesLength(byte[] imageBytes) {
		if (imageBytes.length == 0) {
			throw new IllegalArgumentException("Cannot accept empty picture byte[] as a profile picture");
		}
	}

	private String guessContentType(byte[] imageBytes) throws IOException {
		return URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageBytes));
	}
	
}