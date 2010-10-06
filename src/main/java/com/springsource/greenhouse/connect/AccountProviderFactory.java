package com.springsource.greenhouse.connect;

import java.util.List;

public interface AccountProviderFactory {
	
	<A> AccountProvider<A> getAccountProvider(Class<A> apiType);
	
	AccountProvider<?> getAccountProviderByName(String name);

	List<ConnectedProfile> findConnectedProfiles(Long accountId);
}
