package com.springsource.greenhouse.members;

import java.io.IOException;

public interface ProfilePictureService {

	void saveProfilePicture(Long accountId, byte[] imageBytes) throws IOException;
	
	void saveProfilePicture(Long accountId, byte[] imageBytes, String contentType) throws IOException;

}