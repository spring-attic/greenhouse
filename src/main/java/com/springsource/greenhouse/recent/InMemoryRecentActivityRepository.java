package com.springsource.greenhouse.recent;

import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryRecentActivityRepository implements RecentActivityRepository {

	public List<RecentActivity> findInitial() {
		return Collections.emptyList();
	}

	public RecentActivity findNext(DateTime last) {
		return null;
	}

}
