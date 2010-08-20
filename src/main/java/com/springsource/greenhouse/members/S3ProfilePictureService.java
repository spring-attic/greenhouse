package com.springsource.greenhouse.members;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.jets3t.service.S3ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.s3.S3Operations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.springsource.greenhouse.utils.ImageUtils;

@Service
public class S3ProfilePictureService implements ProfilePictureService {

	private final S3Operations s3;

	@Value("#{s3Properties['s3.bucket']}")
	private String bucketName;

	@Inject
	public S3ProfilePictureService(S3Operations s3) {
		this.s3 = s3;
	}

	@Async
	public void saveProfilePicture(Long accountId, byte[] imageBytes) {
		try {
			saveProfilePicture(accountId, imageBytes, guessContentType(imageBytes));
		} catch (IOException e) {
			throw new ProfilePictureException("Unable to determine content type of image data.", e);
		}
	}

	@Async
	public void saveProfilePicture(Long accountId, byte[] imageBytes, String contentType) {
		assertContentType(contentType);
		assertBytesLength(imageBytes);
		try {
			s3.saveFile(bucketName, "profile-pics/" + accountId + "/normal.jpg", ImageUtils.scaleImageToWidth(imageBytes, 100), contentType);
			s3.saveFile(bucketName, "profile-pics/" + accountId + "/small.jpg", ImageUtils.scaleImageToWidth(imageBytes, 50), contentType);
			s3.saveFile(bucketName, "profile-pics/" + accountId + "/large.jpg", ImageUtils.scaleImageToWidth(imageBytes, 200), contentType);
		} catch (S3ServiceException e) {
			throw new ProfilePictureException("Error saving profile picture.", e);
		} catch (IOException e) {
			throw new ProfilePictureException("Error scaling profile picture.", e);
		}
	}

	private static final List<String> IMAGE_TYPES = Arrays.asList("image/jpeg", "image/gif", "image/png");

	private void assertContentType(String contentType) {
		if (!IMAGE_TYPES.contains(contentType)) {
			throw new IllegalArgumentException("Cannot accept content type '" + contentType + "' as profile picture.");
		}
	}
	
	private void assertBytesLength(byte[] imageBytes) {
		if (imageBytes.length == 0) {
			throw new IllegalArgumentException("Cannot accept empty picture byte[] as profile picture");
		}
	}

	private String guessContentType(byte[] imageBytes) throws IOException {
		return URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageBytes));
	}
	
}