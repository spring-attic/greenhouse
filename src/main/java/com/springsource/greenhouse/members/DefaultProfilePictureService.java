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
public class DefaultProfilePictureService implements ProfilePictureService {
	
	private final S3Operations s3;

	@Value("#{s3Properties['s3.bucket']}")
	private String bucketName;

	@Inject
	public DefaultProfilePictureService(S3Operations s3) {
		this.s3 = s3;
	}
	
	@Async
	public void setProfilePicture(Long accountId, byte[] imageBytes) throws ProfilePictureException {
		try {
			setProfilePicture(accountId, imageBytes, guessContentType(imageBytes));
        } catch (IOException e) {
        		throw new ProfilePictureException("Unable to determine content type of image data.");
        }
	}

	@Async
	public void setProfilePicture(Long accountId, byte[] imageBytes, String contentType) throws ProfilePictureException {		
		validateContentType(contentType);
		try {
			if (imageBytes.length > 0) {
				s3.saveFile(bucketName, "profile-pics/" + accountId + "/normal.jpg", 
						ImageUtils.scaleImageToWidth(imageBytes, 100), contentType);				
				s3.saveFile(bucketName, "profile-pics/" + accountId + "/small.jpg", 
						ImageUtils.scaleImageToWidth(imageBytes, 50), contentType);				
				s3.saveFile(bucketName, "profile-pics/" + accountId + "/large.jpg", 
						ImageUtils.scaleImageToWidth(imageBytes, 200), contentType);
			}  else {
				throw new ProfilePictureException("Error saving profile picture.");
			}
		} catch (S3ServiceException e) {
			throw new ProfilePictureException("Error saving profile picture.", e);
		} catch (IOException e) {
			throw new ProfilePictureException("Error scaling profile picture.", e);
		}
	}
	
	private static final List<String> IMAGE_TYPES = Arrays.asList("image/jpeg", "image/gif", "image/png");	
	private void validateContentType(String contentType) throws ProfilePictureException {
		if (!IMAGE_TYPES.contains(contentType)) {
			throw new ProfilePictureException("Cannot accept content type " + contentType + " as profile picture.");
		}
    }

	private String guessContentType(byte[] imageBytes) throws IOException {
		return URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageBytes));
    }
}
