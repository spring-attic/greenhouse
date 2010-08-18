package org.springframework.integration.comet;

import org.springframework.http.HttpHeaders;
import org.springframework.integration.Message;
import org.springframework.integration.mapping.HeaderMapper;

public class HttpBroadcastMessage {

	private final Message<?> message;
	
	private final boolean extractPayload;
	
	private final HeaderMapper<HttpHeaders> headerMapper;
	
	public HttpBroadcastMessage(Message<?> message, boolean extractPayload, HeaderMapper<HttpHeaders> headerMapper) {
		this.message = message;
		this.extractPayload = extractPayload;
		this.headerMapper = headerMapper;
	}

	public boolean isExtractPayload() {
		return extractPayload;
	}

	public Message<?> getMessage() {
		return message;
	}
	
	public HeaderMapper<HttpHeaders> getHeaderMapper() {
		return headerMapper;
	}
}
