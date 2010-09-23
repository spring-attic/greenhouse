package com.springsource.greenhouse.develop;

import java.util.List;

import com.springsource.greenhouse.connect.NoSuchAccountConnectionException;

public interface AppRepository {

	List<AppSummary> findAppSummaries(Long accountId);

	App findAppBySlug(Long accountId, String slug);

	App findAppByApiKey(String apiKey) throws InvalidApiKeyException;
	
	String updateApp(Long accountId, String slug, AppForm form);

	void deleteApp(Long accountId, String slug);

	AppForm getNewAppForm();
	
	AppForm getAppForm(Long accountId, String slug);

	String createApp(Long accountId, AppForm form);
	
	AppConnection connectApp(Long accountId, String apiKey) throws InvalidApiKeyException;

	AppConnection findAppConnection(String accessToken) throws NoSuchAccountConnectionException;
	
	void disconnectApp(Long accountId, String accessToken);
	
}