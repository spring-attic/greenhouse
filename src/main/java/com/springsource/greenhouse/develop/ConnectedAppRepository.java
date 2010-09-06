package com.springsource.greenhouse.develop;

import java.util.List;

public interface ConnectedAppRepository {

	List<ConnectedAppSummary> findConnectedApps(Long accountId);

	ConnectedApp findConnectedApp(Long accountId, String slug);
	
	String createConnectedApp(ConnectedAppForm form);
	
}
