package com.springsource.greenhouse.connect;

import static java.util.Arrays.*;

import java.io.Serializable;
import java.util.List;

import org.springframework.social.connect.ServiceProvider;
import org.springframework.social.connect.ServiceProviderConnection;

public class StubServiceProvider<S> implements ServiceProvider<S> {


	private ServiceProviderConnection<S> connection;

	public StubServiceProvider(S serviceApi) {
		connection = new StubServiceProviderConnection<S>(serviceApi);
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConnected(Serializable accountId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ServiceProviderConnection<S>> getConnections(Serializable accountId) {
		return asList(connection);
	}

}
