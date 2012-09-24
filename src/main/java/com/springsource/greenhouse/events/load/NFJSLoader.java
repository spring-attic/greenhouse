/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.events.load;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class NFJSLoader {
	
	private static final Logger logger = LoggerFactory.getLogger(NFJSLoader.class);

	private final RestTemplate restTemplate;
	private final EventLoaderRepository loaderRepository;
	private final Map<Long, Long> timeSlotIdMap;
	private final Map<Long, Long> leaderIdMap;
	private final Map<Long, Long> topicSlotMap;

	@Inject
	public NFJSLoader(EventLoaderRepository loaderRepository) {
		this.loaderRepository = loaderRepository;
		this.restTemplate = new RestTemplate();
		this.timeSlotIdMap = new HashMap<Long, Long>();
		this.leaderIdMap = new HashMap<Long, Long>();
		this.topicSlotMap = new HashMap<Long, Long>();
	}

	public void loadEventData(int showId) {
		@SuppressWarnings("unchecked")
		Map<String, Object> eventMap = (Map<String, Object>) restTemplate.getForObject("https://springone2gx.com/m/data/show_short.json?showId={showId}", Map.class, showId);
		long sourceId = (Integer) eventMap.get("id");
		String name = (String) eventMap.get("name");
		String firstDay = (String) eventMap.get("firstDay");
		String lastDay = (String) eventMap.get("lastDay");
		String abbreviation = (String) eventMap.get("abreviation"); // yes, that's correct
		@SuppressWarnings("unchecked")
		Map<String, Object> locationMap = (Map<String, Object>) eventMap.get("location");
		String timeZone = (String) locationMap.get("timeZoneName");
		int utcOffset = (Integer) locationMap.get("utcOffset");
		String locationAddress = locationMap.get("address1") + " " + locationMap.get("address2") + " " + locationMap.get("city") + ", " + locationMap.get("stateCode") + " " + locationMap.get("zip");
		long eventId = loaderRepository.loadEvent(new EventData(MEMBER_GROUP_ID, name, null, abbreviation, firstDay, lastDay, timeZone, PROVIDER_ID, sourceId),
				new VenueData((String) locationMap.get("description"), (String) locationAddress, (Double) locationMap.get("latitude"), (Double) locationMap.get("longitude"), (String) locationMap.get("metroArea")));
		loadTimeSlotData(showId, eventId, utcOffset);
		loadLeaderData(showId);
		loadEventSessionData(showId, eventId, abbreviation);
	}
	
	private void loadLeaderData(int showId) {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> leaderMapList = (List<Map<String, Object>>) restTemplate.getForObject("https://springone2gx.com/m/data/show_speakers.json?showId={showId}", List.class, showId);
		for (Map<String, Object> leaderMap : leaderMapList) {	
			long sourceId = (Integer) leaderMap.get("id");
			String name = (String) leaderMap.get("name");
			String bio = (String) leaderMap.get("bio");
			String personalUrl = (String) leaderMap.get("blogLink");
			String twitterId = (String) leaderMap.get("twitterId");
			LeaderData leaderData = new LeaderData(name, bio, personalUrl, twitterId, PROVIDER_ID, sourceId);
			long leaderId = loaderRepository.loadLeader(leaderData);
			leaderIdMap.put(sourceId, leaderId);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadTimeSlotData(int showId, long eventId, int utcOffset) {
		List<Map<String, Object>> dayMapList = (List<Map<String, Object>>) restTemplate.getForObject("https://springone2gx.com/m/data/show_schedule.json?showId={showId}", List.class, showId);
		for (Map<String, Object> dayMap : dayMapList) {
			List<Map<String, Object>> slotMapList = (List<Map<String, Object>>) dayMap.get("slots");
			for (Map<String, Object> slotMap : slotMapList) {
				long sourceId = (Integer) slotMap.get("id");
				String label = (String) slotMap.get("label");
				String startTime = (String) slotMap.get("startTime");
				String endTime = (String) slotMap.get("endTime");
				TimeSlotData timeSlotData = new TimeSlotData(eventId, label, startTime, endTime, PROVIDER_ID, sourceId);
				long timeSlotId = loaderRepository.loadTimeSlot(timeSlotData);	
				timeSlotIdMap.put(sourceId, timeSlotId);
				List<Map<String, Object>> presentations = (List<Map<String, Object>>) slotMap.get("presentations");
				for (Map<String, Object> presentation : presentations) {
					if (presentation.get("topicId") != null && presentation.get("slotId") != null) {
						long topicId = (Integer) presentation.get("topicId");
						long slotId = (Integer) presentation.get("slotId");
						topicSlotMap.put(topicId, slotId);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadEventSessionData(int showId, long event, String abbreviation) {
		List<Map<String, Object>> topicMapList = (List<Map<String, Object>>) restTemplate.getForObject("https://springone2gx.com/m/data/show_topics.json?showId={showId}", List.class, showId);
		for (Map<String, Object> topicMap : topicMapList) {
			long sourceId = (Integer) topicMap.get("id");
			String title = (String) topicMap.get("title");
			String description = (String) topicMap.get("summary");
			String hashtag = "#" + abbreviation + sourceId;
			Long venue = null; // TODO: Figure out how to get this from event
			Long sourceTimeslot = topicSlotMap.get(sourceId);
			Long timeslot = timeSlotIdMap.get(sourceTimeslot);
			List<Integer> speakerIds = (List<Integer>) topicMap.get("speakerIds");
			List<Long> leaderIds = new ArrayList<Long>();
			for (Integer speakerId : speakerIds) {
				Long leaderId = leaderIdMap.get(speakerId.longValue());
				if (leaderId != null) {
					leaderIds.add(leaderId);
				} else {
					logger.warn("Unknown speaker ID " + speakerId + ". Not in show_speakers.json feed.");
				}
			}
			
			EventSessionData eventSessionData = new EventSessionData(event, -1, title, description, hashtag, venue, timeslot, PROVIDER_ID, sourceId, leaderIds);
			loaderRepository.loadEventSession(eventSessionData);
		}
	}
	
	// expose getter so that we can use Spring Test MVC to mock NFJS server
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}
	
	private static final String PROVIDER_ID = "NFJS";
	private static final long MEMBER_GROUP_ID = 1;

}
