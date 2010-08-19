package com.springsource.greenhouse.members;

// TODO consider unchecked exceptions instead of checked
// or make checked exceptions more specific if recoverable
public interface ProfilePictureService {

	void setProfilePicture(Long accountId, byte[] imageBytes) throws ProfilePictureException;
	
	void setProfilePicture(Long accountId, byte[] imageBytes, String contentType) throws ProfilePictureException;

}