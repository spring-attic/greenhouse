package com.springsource.greenhouse.connect;

public interface AccountProviderFactory {
	
	AccountProvider<?> getAccountProvider(String name);

	<A> AccountProvider<A> getAccountProvider(String name, Class<A> apiType);

}
