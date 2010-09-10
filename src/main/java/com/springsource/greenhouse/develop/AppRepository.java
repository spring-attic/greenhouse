package com.springsource.greenhouse.develop;

import java.util.List;

public interface AppRepository {

	List<AppSummary> findAppSummaries(Long accountId);

	App findApp(Long accountId, String slug);
	
	String updateApp(Long accountId, String slug, AppForm form);

	void deleteApp(Long accountId, String slug);

	AppForm getNewAppForm();
	
	AppForm getAppForm(Long accountId, String slug);

	String createApp(Long accountId, AppForm form);
}
