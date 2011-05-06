package com.springsource.greenhouse.events;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class EventSessionForm {
	
	@NotEmpty
	private String title;
	
	private String hashtag;

	private DateFields startTimeFields = new DateFields();
	
	private DateFields endTimeFields = new DateFields();

	private String description;

	@NotEmpty
	private String trackCode;

	private LeaderFields leaderFields = new LeaderFields();
	
	/**
	 * Title of the session.
	 */
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Twitter hash tag identifying the session relative to its event.
	 */
	public String getHashtag() {
		return hashtag;
	}
	
	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	/**
	 * Fields to complete to enter session start time.
	 */
	public DateFields getStartTimeFields() {
		return startTimeFields;
	}

	/**
	 * Fields to complete to enter session end time.
	 */
	public DateFields getEndTimeFields() {
		return endTimeFields;
	}

	/**
	 * The start time of the event sessiun, calculated from the completed field values.
	 */
	public DateTime getStartTime(DateTimeZone eventTimeZone) {
		return startTimeFields.getDateTime(eventTimeZone);
	}

	/**
	 * The end time of the event session, calculated from the completed field values.
	 */
	public DateTime getEndTime(DateTimeZone eventTimeZone) {
		return endTimeFields.getDateTime(eventTimeZone);
	}
	
	/**
	 * Session abstract.
	 */
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Track associated with the session.
	 */
	public String getTrackCode() {
		return trackCode;
	}
	
	public void setTrackCode(String trackCode) {
		this.trackCode = trackCode;
	}

	public LeaderFields getLeaderFields() {
		return leaderFields;
	}
	
}