package com.springsource.greenhouse.account;

import javax.inject.Inject;

import org.jets3t.service.S3ServiceException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.s3.S3Operations;
import org.springframework.stereotype.Service;

@Service
public class JdbcProfilePictureService implements ProfilePictureService {
	
	private final S3Operations s3;
	private final JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcProfilePictureService(S3Operations s3, JdbcTemplate jdbcTemplate) {
		this.s3 = s3;
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void setProfilePicture(Long accountId, byte[] imageBytes, String contentType) throws ProfilePictureException {
		// TODO: Probably should reject non-image files
		try {
			if(imageBytes.length > 0) {
				// TODO: Figure out real extension from mime type or original file name
				String imageUrl = s3.saveFile("gh-images", "profilepix/" + accountId + ".jpg", 
						imageBytes, contentType);
								
				jdbcTemplate.update("update member set imageUrl = ? where id = ?", imageUrl, accountId);
			}  else {
				throw new ProfilePictureException("Error saving profile picture.");
			}
		} catch (S3ServiceException e) {
			throw new ProfilePictureException("Error saving profile picture.", e);
		}
	}
}
