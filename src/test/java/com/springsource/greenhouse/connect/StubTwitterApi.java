package com.springsource.greenhouse.connect;

import static org.junit.Assert.*;

import java.util.List;

import org.springframework.social.twitter.DirectMessage;
import org.springframework.social.twitter.SearchResults;
import org.springframework.social.twitter.StatusDetails;
import org.springframework.social.twitter.Tweet;
import org.springframework.social.twitter.TwitterApi;
import org.springframework.social.twitter.TwitterProfile;

public class StubTwitterApi implements TwitterApi {

	private String status;

	@Override
	public String getProfileId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TwitterProfile getUserProfile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TwitterProfile getUserProfile(String screenName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TwitterProfile getUserProfile(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getFriends(String screenName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateStatus(String status) {
		this.status = status;
	}

	public void verifyStatus(String expected) {
		assertEquals(expected, status);
	}

	@Override
	public void updateStatus(String status, StatusDetails details) {
		// TODO Auto-generated method stub
	}

	@Override
	public void retweet(long tweetId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Tweet> getMentions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DirectMessage> getDirectMessagesReceived() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendDirectMessage(String toScreenName, String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendDirectMessage(long toUserId, String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Tweet> getPublicTimeline() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tweet> getHomeTimeline() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tweet> getFriendsTimeline() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tweet> getUserTimeline() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tweet> getUserTimeline(String screenName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tweet> getUserTimeline(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults search(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults search(String query, int page, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchResults search(String query, int page, int pageSize, int sinceId, int maxId) {
		// TODO Auto-generated method stub
		return null;
	}

}
