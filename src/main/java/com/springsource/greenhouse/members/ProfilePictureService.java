package com.springsource.greenhouse.members;

public interface ProfilePictureService {

	void saveProfilePicture(Long accountId, byte[] imageBytes);
	
	void saveProfilePicture(Long accountId, byte[] imageBytes, String contentType);

}