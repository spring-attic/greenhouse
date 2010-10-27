package com.springsource.greenhouse.members;

import java.io.IOException;

public interface ProfilePictureService {

	void saveProfilePicture(Long accountId, byte[] imageBytes) throws IOException;
	
}