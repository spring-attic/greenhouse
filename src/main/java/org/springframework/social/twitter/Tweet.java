package org.springframework.social.twitter;

import java.util.Date;

public class Tweet {
	private long id;
	private String text;
	private Date createdAt;
	private String fromUser;
	private String profileImageUrl;
	private Long toUserId;
	private long fromUserId;
	private String languageCode;
	private String source;

	/*
	 * TO CONSIDER: Tweets may also have extra metadata and geolocation
	 * information. I'm leaving those out for now, because very few tweets seem
	 * to have much useful for those fields. After some bit of searching, I
	 * did find a few tweets that had geolocation that resembled the following:
	 * 
	 * geo: { type: "Point", coordinates: [14.6167, 120.9667] }
	 * 
	 * But I'm unclar on what other types there may be, what that data may look
	 * like, and whether it's even important given that such a relative few
	 * tweets will have that info in it.
	 */

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public Long getToUserId() {
		return toUserId;
	}

	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}

	public long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(long fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
