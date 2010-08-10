package com.springsource.greenhouse.account;

public interface ProfilePictureService {

	void setProfilePicture(Long accountId, byte[] imageBytes, String contentType) throws ProfilePictureException;

}