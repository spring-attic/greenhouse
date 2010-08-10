package com.springsource.greenhouse.recent;

import java.util.List;

import org.joda.time.DateTime;

public interface RecentActivityRepository {

	List<RecentActivity> findInitial();
	
	RecentActivity findNext(DateTime last);

}
