package org.springframework.integration.comet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.MessageDeliveryException;
import org.springframework.integration.MessageHandlingException;
import org.springframework.integration.MessageRejectedException;
import org.springframework.integration.core.MessageHandler;
import org.springframework.integration.core.PollableChannel;
import org.springframework.integration.core.SubscribableChannel;
import org.springframework.integration.endpoint.AbstractEndpoint;
import org.springframework.integration.endpoint.EventDrivenConsumer;
import org.springframework.integration.endpoint.PollingConsumer;
import org.springframework.integration.http.DefaultHttpHeaderMapper;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestHandler;
 
public class AsyncHttpRequestHandlingMessageAdapter extends AbstractEndpoint
		implements MessageHandler, HttpRequestHandler {
	
	public static final String ENDPOINT_PATH_HEADER = "endpoint-path";
	
	private static final Log log = LogFactory.getLog(AsyncHttpRequestHandlingMessageAdapter.class);

	private volatile MessageChannel messageChannel;

	private volatile AbstractEndpoint consumerEndpoint;
	
	private final HttpMessageMapper messageMapper = new HttpMessageMapper();
	
	private volatile HeaderMapper<HttpHeaders> headerMapper = new DefaultHttpHeaderMapper();

	private volatile Class<?> requestPayloadType = null;
	
	private volatile boolean extractResponsePayload = true;
	
	//Hopefully this threshold handling is temporary until we get the BroadcasterCache working as it should
	private volatile int messageThreshold = 0;
	
	private volatile BlockingQueue<HttpBroadcastMessage> messageQueue;
	
	public void handleMessage(Message<?> message)
			throws MessageRejectedException, MessageHandlingException,
			MessageDeliveryException {
		try {
			HttpBroadcastMessage httpMessage = new HttpBroadcastMessage(MessageBuilder.fromMessage(message).setHeaderIfAbsent(ENDPOINT_PATH_HEADER, 
					this.getComponentName()).build(), this.extractResponsePayload, this.headerMapper);
			messageQueue.add(httpMessage);
			if (messageQueue.size() >= messageThreshold) {
				Broadcaster broadcaster = BroadcasterFactory.getDefault().lookup(DefaultBroadcaster.class, this.getComponentName());
				if (broadcaster == null) {
					//TODO - This will potentially cause lost messages...fix that
					log.warn("Message received but no Broadcaster available.");
					return;
				}
				List<HttpBroadcastMessage> broadcastMessages = new ArrayList<HttpBroadcastMessage>();
				messageQueue.drainTo(broadcastMessages);
				if (broadcastMessages.size() > 0) {
					if (log.isInfoEnabled()) {
						log.info("Broadcasting message "+message.toString()+" to "+broadcaster.getAtmosphereResources().size()+ " suspended resources.");
					}
					Future<Object> future = broadcaster.broadcast(broadcastMessages);
					if (future != null) {
						future.get();
					}
					if (log.isDebugEnabled()) {
						log.debug("Broadcast operation for "+broadcastMessages+" executed.");
					}
				}
			}
		} catch (Exception ex) {
			throw new IllegalStateException("Broadcast failed", ex);
		}
	}

	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		AtmosphereResource<HttpServletRequest, HttpServletResponse> resource = getAtmosphereResource(request);
		
		if (!this.getComponentName().equals(resource.getBroadcaster().getID())) {
			resource.getBroadcaster().setID(this.getComponentName());
		}
		
		if (request.getMethod().equalsIgnoreCase("GET")) {
			subscribe(resource);
		} else if (request.getMethod().equalsIgnoreCase("POST")){
			publish(resource);
		}
	}

	@Override
	protected void onInit() throws Exception {
		super.onInit();
		if (this.messageChannel instanceof PollableChannel) {
			this.consumerEndpoint = new PollingConsumer(
					(PollableChannel) this.messageChannel, this);
		} else if (this.messageChannel instanceof SubscribableChannel) {
			this.consumerEndpoint = new EventDrivenConsumer(
					(SubscribableChannel) this.messageChannel, this);
		}
	}

	private void subscribe(
			AtmosphereResource<HttpServletRequest, HttpServletResponse> resource) {
		if (log.isInfoEnabled()) {
			log.info("Handling subscription request for resource with broadcaster ID: "+resource.getBroadcaster().getID());
		}
		//Write a content type (for all future responses) as best guessed from the configured converters, since this is our
		//only chance to write the headers.
		ServletServerHttpRequest request = new ServletServerHttpRequest(resource.getRequest());
		ServletServerHttpResponse response = new ServletServerHttpResponse(resource.getResponse());
		for (HttpMessageConverter<?> converter : this.messageMapper.getMessageConverters()) {
			for (MediaType acceptType : request.getHeaders().getAccept()) {
				for (MediaType supportedMediaType : converter.getSupportedMediaTypes()) {
					if (!MediaType.ALL.equals(supportedMediaType) && supportedMediaType.isCompatibleWith(acceptType)) {
						if (!supportedMediaType.isWildcardType() && !supportedMediaType.isWildcardSubtype()) {
							response.getHeaders().setContentType(supportedMediaType);
						} else {
							//Try to guess the "default" type for the converter
							MediaType defaultType = converter.getSupportedMediaTypes().get(0);
							if (!defaultType.isWildcardType() && !defaultType.isWildcardSubtype()) {
								response.getHeaders().setContentType(defaultType);
							}
						}
						doSuspend(resource);
						if (log.isInfoEnabled()) {
							log.info("Resource with broadcaster ID: "+resource.getBroadcaster().getID()+" suspended.");
						}
						return;							
					}
				}
			}
		}
		
		doSuspend(resource);
		
		if (log.isInfoEnabled()) {
			log.info("Resource with broadcaster ID: "+resource.getBroadcaster().getID()+" suspended without writing a Content-Type.");
		}
	}

	private void doSuspend(
			AtmosphereResource<HttpServletRequest, HttpServletResponse> resource) {
		boolean flushComment = isFlushCommentsRequired(resource.getRequest());
		setResumeOnBroadcast(resource.getRequest());
		
		//TODO - Allow setting an explicit long-poll timeout
		resource.suspend(-1L, flushComment);
	}

	private boolean isFlushCommentsRequired(HttpServletRequest request) {
		String upgrade = request.getHeader("Connection");
        if (StringUtils.hasText(upgrade) && upgrade.equalsIgnoreCase("Upgrade")) {
           return false;
        } else if (isLongPolling(request)) {
            return false;
        }
        return true;
	}

	private void setResumeOnBroadcast(HttpServletRequest request) {
		if (isLongPolling(request)) {
			request.setAttribute(AtmosphereServlet.RESUME_ON_BROADCAST, new Boolean(true));
        }
	}
	
	private boolean isLongPolling(HttpServletRequest request) {
		String transport = request.getHeader("X-Atmosphere-Transport");
		return StringUtils.hasText(transport) && transport.equals("long-polling");
	}

	private void publish(
			AtmosphereResource<HttpServletRequest, HttpServletResponse> resource) {
		Message<?> message;
		try {
			message = messageMapper.readMessage(resource.getRequest(), this.requestPayloadType, this.headerMapper);
		} catch (Exception ex) {
			throw new IllegalStateException("Could not read request body.", ex);
		}
		this.messageChannel.send(message);
	}

	@SuppressWarnings("unchecked")
	private AtmosphereResource<HttpServletRequest, HttpServletResponse> getAtmosphereResource(
			HttpServletRequest request) {
		AtmosphereResource<HttpServletRequest, HttpServletResponse> resource = (AtmosphereResource<HttpServletRequest, HttpServletResponse>) request
				.getAttribute(AtmosphereServlet.ATMOSPHERE_RESOURCE);
		Assert
				.notNull(
						resource,
						"AtmosphereResource could not be located for the request.  Check that AtmosphereServlet is configured correctly in web.xml");
		return resource;
	}

	@Override
	protected void doStart() {
		this.messageQueue = new LinkedBlockingQueue<HttpBroadcastMessage>();
		this.consumerEndpoint.start();
	}

	@Override
	protected void doStop() {
		this.consumerEndpoint.stop();
	}

	public void setMessageChannel(MessageChannel messageChannel) {
		this.messageChannel = messageChannel;
	}

	public void setMessageThreshold(int messageThreshold) {
		this.messageThreshold = messageThreshold;
	}
}
