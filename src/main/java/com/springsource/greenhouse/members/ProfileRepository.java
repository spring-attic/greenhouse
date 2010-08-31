package com.springsource.greenhouse.members;

import java.util.List;

import org.springframework.social.account.PictureSize;


public interface ProfileRepository {

	Profile findByKey(String profileKey);

	Profile findByAccountId(Long accountId);

	List<ConnectedProfile> findConnectedProfiles(Long accountId);

	String findProfilePictureUrl(String profileKey, PictureSize size);
	
}