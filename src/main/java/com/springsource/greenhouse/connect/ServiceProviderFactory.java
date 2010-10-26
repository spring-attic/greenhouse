package com.springsource.greenhouse.connect;

public interface ServiceProviderFactory {
	
	ServiceProvider<?> getServiceProvider(String name);

	<A> ServiceProvider<A> getServiceProvider(String name, Class<A> apiType);

}
