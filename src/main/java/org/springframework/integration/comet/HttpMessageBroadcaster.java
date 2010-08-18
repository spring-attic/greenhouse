package org.springframework.integration.comet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
import org.springframework.integration.MessagingException;

public class HttpMessageBroadcaster extends DefaultBroadcaster {

	private static final Log log = LogFactory.getLog(HttpMessageBroadcaster.class);
	
	private final HttpMessageMapper messageMapper = new HttpMessageMapper();
	
	@SuppressWarnings("unchecked")
	@Override
	protected void broadcast(AtmosphereResource<?, ?> resource, AtmosphereResourceEvent event) {
		/*event.setMessage("1234567");
		super.broadcast(resource, event);
		return;*/
		if (!HttpBroadcastMessage.class.isAssignableFrom(event.getMessage().getClass())) {
			super.broadcast(resource, event);
			return;
		}
		HttpBroadcastMessage broadcastMessage = (HttpBroadcastMessage) event.getMessage();
		HttpServletRequest request = (HttpServletRequest) resource.getRequest();
		HttpServletResponse response = (HttpServletResponse) resource.getResponse();
		try {
			response.getOutputStream();
		} catch (Exception ex) {
			throw new MessagingException("Cannot get the Servlet OutputStream for delivering async Message to browser client.  " +
					"Ensure that you've set 'useStreamForFlushingComments' to true in the AtmosphereServlet.", ex);
		}
		
		HttpMessageBroadcasterResponseWrapper responseWrapper = new HttpMessageBroadcasterResponseWrapper(response); 
		messageMapper.writeMessage(request, responseWrapper, broadcastMessage.getMessage(), broadcastMessage.isExtractPayload(), broadcastMessage.getHeaderMapper());
		try {
			response.getOutputStream().write(responseWrapper.toByteArray());
			if (log.isDebugEnabled()) {
				log.debug("Wrote "+responseWrapper.toByteArray().length+" bytes to response.");
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
