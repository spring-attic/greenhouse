package com.springsource.greenhouse.connect;

public interface AccountProviderRepository {
	
	AccountProvider findAccountProviderByName(String name);
	
}
