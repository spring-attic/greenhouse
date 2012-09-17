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



/**
 * Data access interface for loading {@link Event} data.
 * @author Craig Walls
 */
public interface EventLoaderRepository {

	long loadEvent(EventData eventData, VenueData venueData);
		
	long loadLeader(LeaderData leaderData);

	long loadTimeSlot(TimeSlotData timeSlotData);

	int loadEventSession(EventSessionData eventSessionData);

}