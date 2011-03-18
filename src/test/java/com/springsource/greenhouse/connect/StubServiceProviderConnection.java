package com.springsource.greenhouse.connect;

import org.springframework.social.connect.ServiceProviderConnection;

public class StubServiceProviderConnection<S> implements ServiceProviderConnection<S> {

	private final S serviceApi;

	public StubServiceProviderConnection(S serviceApi) {
		this.serviceApi = serviceApi;
	}

	public S getServiceApi() {
		return serviceApi;
	}

	public void disconnect() {
	}

}
