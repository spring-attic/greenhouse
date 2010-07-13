package org.springframework.social.twitter;

import java.util.List;

public class SearchResults {
	private List<Tweet> tweets;
	private long maxId;
	private long sinceId;
	private boolean lastPage;

	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweet> results) {
		this.tweets = results;
	}

	public long getMaxId() {
		return maxId;
	}

	public void setMaxId(long maxId) {
		this.maxId = maxId;
	}

	public long getSinceId() {
		return sinceId;
	}

	public void setSinceId(long sinceId) {
		this.sinceId = sinceId;
	}

	public boolean isLastPage() {
		return lastPage;
	}

	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}
}
