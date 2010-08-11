package com.springsource.greenhouse.members;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.jets3t.service.S3ServiceException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.s3.S3Operations;
import org.springframework.stereotype.Service;

// TODO consider extracting out the persisting of the pictureUrl
@Service
public class JdbcProfilePictureService implements ProfilePictureService {
	
	private final S3Operations s3;
	
	private final JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcProfilePictureService(S3Operations s3, JdbcTemplate jdbcTemplate) {
		this.s3 = s3;
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public String setProfilePicture(Long accountId, byte[] imageBytes) throws ProfilePictureException {
		try {
			return setProfilePicture(accountId, imageBytes, guessContentType(imageBytes));
        } catch (IOException e) {
        		throw new ProfilePictureException("Unable to determine content type of image data.");
        }
	}

	public String setProfilePicture(Long accountId, byte[] imageBytes, String contentType) throws ProfilePictureException {		
		validateContentType(contentType);
		try {
			if (imageBytes.length > 0) {
				String pictureUrl = s3.saveFile(
						"gh-images", "profile-pics/" + accountId + IMAGE_TYPE_EXTENSIONS.get(contentType), 
						imageBytes, contentType);					
				jdbcTemplate.update("update member set pictureUrl = ? where id = ?", pictureUrl, accountId);
				return pictureUrl;
			}  else {
				throw new ProfilePictureException("Error saving profile picture.");
			}
		} catch (S3ServiceException e) {
			throw new ProfilePictureException("Error saving profile picture.", e);
		}
	}

	private static final Map<String, String> IMAGE_TYPE_EXTENSIONS =new HashMap<String, String>();
	static {
		IMAGE_TYPE_EXTENSIONS.put("image/jpeg", ".jpg");
		IMAGE_TYPE_EXTENSIONS.put("image/gif", ".gif");
		IMAGE_TYPE_EXTENSIONS.put("image/png", ".png");
	}
	
	private void validateContentType(String contentType) throws ProfilePictureException {
		if (!IMAGE_TYPE_EXTENSIONS.keySet().contains(contentType)) {
			throw new ProfilePictureException("Cannot accept content type " + contentType + " as profile picture.");
		}
    }

	private String guessContentType(byte[] imageBytes) throws IOException {
		return URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(imageBytes));
    }
}
