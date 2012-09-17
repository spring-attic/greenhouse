package com.springsource.greenhouse.events.load;

class TimeSlotData {

	private final long eventId;
	private final String label;
	private final String startTime;
	private final String endTime;
	private final String source;
	private final long sourceId;

	public TimeSlotData(long eventId, String label, String startTime, String endTime, String source, long sourceId) {
		this.eventId = eventId;
		this.label = label;
		this.startTime = startTime;
		this.endTime = endTime;
		this.source = source;
		this.sourceId = sourceId;		
	}
	
	public long getEventId() {
		return eventId;
	}

	public String getLabel() {
		return label;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}
	
	public String getSource() {
		return source;
	}
	
	public long getSourceId() {
		return sourceId;
	}
}
