package com.springsource.greenhouse.members;

import java.util.List;

public interface ProfileRepository {

	Profile findByKey(String profileKey);

	Profile findByAccountId(Long accountId);

	List<ConnectedProfile> findConnectedProfiles(Long accountId);
	
}