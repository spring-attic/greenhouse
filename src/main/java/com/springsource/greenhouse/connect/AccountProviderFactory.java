package com.springsource.greenhouse.connect;

public interface AccountProviderFactory {
	
	<A> AccountProvider<A> getAccountProvider(Class<A> apiType);
	
	AccountProvider<?> getAccountProviderByName(String name);
}
