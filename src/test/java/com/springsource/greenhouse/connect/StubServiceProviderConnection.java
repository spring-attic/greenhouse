package com.springsource.greenhouse.connect;

import org.springframework.social.connect.ServiceProviderConnection;

public class StubServiceProviderConnection<S> implements ServiceProviderConnection<S> {

	private final S serviceApi;

	public StubServiceProviderConnection(S serviceApi) {
		this.serviceApi = serviceApi;

	}

	@Override
	public S getServiceApi() {
		return serviceApi;
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

}
