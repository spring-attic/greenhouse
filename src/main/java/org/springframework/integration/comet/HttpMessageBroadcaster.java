/*
 * Copyright 2010 the original author or authors.
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
package org.springframework.integration.comet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.springframework.http.HttpHeaders;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.http.DefaultHttpHeaderMapper;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.integration.support.MessageBuilder;

/**
 * @author Jeremy Grelle
 */
public class HttpMessageBroadcaster extends DefaultBroadcaster {

	private static final Log log = LogFactory.getLog(HttpMessageBroadcaster.class);
	
	private final HttpMessageMapper messageMapper = new HttpMessageMapper();
	
	private volatile HeaderMapper<HttpHeaders> headerMapper = new DefaultHttpHeaderMapper();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void broadcast(AtmosphereResource<?, ?> resource, AtmosphereResourceEvent event) { 
		List<HttpBroadcastMessage> messages = new ArrayList<HttpBroadcastMessage>();
		if (event.getMessage() instanceof List) {
			List messageBacklog = (List) event.getMessage();
			if (!messageBacklog.isEmpty() && messageBacklog.get(0) instanceof HttpBroadcastMessage) {
				messages.addAll(messageBacklog);
			} else {
				super.broadcast(resource, event);
				return;
			}
		} else if (HttpBroadcastMessage.class.isAssignableFrom(event.getMessage().getClass())){
			messages.add((HttpBroadcastMessage) event.getMessage());
		} else {
			super.broadcast(resource, event);
			return;
		}
		
		Message<?> broadcastMessage = mergeMessagesForBroadcast(messages);
		HttpServletRequest request = (HttpServletRequest) resource.getRequest();
		HttpServletResponse response = (HttpServletResponse) resource.getResponse();
		try {
			response.getOutputStream();
		} catch (Exception ex) {
			throw new MessagingException("Cannot get the Servlet OutputStream for delivering async Message to browser client.  " +
					"Ensure that you've set 'useStreamForFlushingComments' to true in the AtmosphereServlet.", ex);
		}
		
		HttpMessageBroadcasterResponseWrapper responseWrapper = new HttpMessageBroadcasterResponseWrapper(response); 
		messageMapper.writeMessage(request, responseWrapper, broadcastMessage, true, headerMapper);
		try {
			response.getOutputStream().write(responseWrapper.toByteArray());
			if (log.isInfoEnabled()) {
				log.info("Wrote "+responseWrapper.toByteArray().length+" bytes to response.");
			}
			response.getOutputStream().flush();
		} catch (IOException ex) {
			throw new MessagingException("Failed to write async Message to browser client.", ex);
		}
		
		Boolean resumeOnBroadcast = (Boolean) request.getAttribute(AtmosphereServlet.RESUME_ON_BROADCAST);
        if (resumeOnBroadcast != null && resumeOnBroadcast) {
            resource.resume();
        }
	}
	
	private Message<?> mergeMessagesForBroadcast(List<HttpBroadcastMessage> messages) {
		List<Object> payloads = new ArrayList<Object>();
		for(HttpBroadcastMessage message : messages) {
			payloads.add(message.getMessage().getPayload());
		}
		return MessageBuilder.withPayload(payloads).build();
	}

	private static class HttpMessageBroadcasterResponseWrapper extends HttpServletResponseWrapper {

			private final ByteArrayOutputStream content = new ByteArrayOutputStream();

			private final ServletOutputStream outputStream = new ResponseServletOutputStream();

			private HttpMessageBroadcasterResponseWrapper(HttpServletResponse response) {
				super(response);
			}

			@Override
			public ServletOutputStream getOutputStream() {
				return this.outputStream;
			}

			@Override
			public void resetBuffer() {
				this.content.reset();
			}

			@Override
			public void reset() {
				super.reset();
				resetBuffer();
			}

			private byte[] toByteArray() {
				return this.content.toByteArray();
			}

			private class ResponseServletOutputStream extends ServletOutputStream {

				@Override
				public void write(int b) throws IOException {
					content.write(b);
				}

				@Override
				public void write(byte[] b, int off, int len) throws IOException {
					content.write(b, off, len);
				}
			}
	}

}
