package org.springframework.integration.comet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.http.MultipartAwareFormHttpMessageConverter;
import org.springframework.integration.http.SerializingHttpMessageConverter;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class HttpMessageMapper {

	private static final Log log = LogFactory.getLog(HttpMessageMapper.class);
	
	private static final boolean jaxb2Present = ClassUtils.isPresent(
			"javax.xml.bind.Binder", HttpMessageMapper.class
					.getClassLoader());

	private static final boolean jacksonPresent = ClassUtils.isPresent(
			"org.codehaus.jackson.map.ObjectMapper",
			HttpMessageMapper.class.getClassLoader())
			&& ClassUtils.isPresent("org.codehaus.jackson.JsonGenerator",
					HttpMessageMapper.class.getClassLoader());

	private static boolean romePresent = ClassUtils.isPresent(
			"com.sun.syndication.feed.WireFeed",
			HttpMessageMapper.class.getClassLoader());

	private volatile List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
	
	@SuppressWarnings("unchecked")
	public HttpMessageMapper() {
		this.messageConverters.add(new MultipartAwareFormHttpMessageConverter());
		this.messageConverters.add(new SerializingHttpMessageConverter());
		if (jacksonPresent) {
			this.messageConverters.add(new MappingJacksonHttpMessageConverter());
		}
		this.messageConverters.add(new ByteArrayHttpMessageConverter());
		this.messageConverters.add(new StringHttpMessageConverter());
		this.messageConverters.add(new ResourceHttpMessageConverter());
		this.messageConverters.add(new SourceHttpMessageConverter());
		if (jaxb2Present) {
			this.messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
		}
		if (romePresent) {
			// TODO add deps for:
			//this.messageConverters.add(new AtomFeedHttpMessageConverter());
			//this.messageConverters.add(new RssChannelHttpMessageConverter());
		}
	}
	
	public Message<?> readMessage(HttpServletRequest servletRequest, Class<?> expectedType, HeaderMapper<HttpHeaders> headerMapper) {
		ServletServerHttpRequest request = new ServletServerHttpRequest(servletRequest);
		Object payload = null;
		if (this.isReadable(request)) {
			payload = this.generatePayloadFromRequestBody(request, null);
		}
		else {
			payload = this.convertParameterMap(servletRequest.getParameterMap());
		}
		Map<String, ?> headers = headerMapper.toHeaders(request.getHeaders());
		return MessageBuilder.withPayload(payload)
				.copyHeaders(headers)
				.setHeader(org.springframework.integration.http.HttpHeaders.REQUEST_URL, request.getURI().toString())
				.setHeader(org.springframework.integration.http.HttpHeaders.REQUEST_METHOD, request.getMethod().toString())
				.setHeader(org.springframework.integration.http.HttpHeaders.USER_PRINCIPAL, servletRequest.getUserPrincipal())
				.build();
	}
	
	@SuppressWarnings("unchecked")
	public void writeMessage(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Message<?> message, boolean extractPayload, HeaderMapper<HttpHeaders> headerMapper) {
		ServletServerHttpRequest request = new ServletServerHttpRequest(servletRequest);
		ServletServerHttpResponse response = new ServletServerHttpResponse(servletResponse);
		Object content = message;
		if(extractPayload) {
			headerMapper.fromHeaders(message.getHeaders(), response.getHeaders());
			content = message.getPayload();
		}
		for (HttpMessageConverter converter : this.getMessageConverters()) {
			if (CollectionUtils.isEmpty(request.getHeaders().getAccept())) {
				//TODO - This is needed because the headers never get set with WebSocket.  Make this configurable.
				request.getHeaders().set("Accept", "application/json");
			}
			for (MediaType acceptType : request.getHeaders().getAccept()) {
				if (converter.canWrite(content.getClass(), acceptType)) {
					try {
						converter.write(content, acceptType, response);
					} catch (Exception e) {
						throw new MessagingException("Could not convert reply: failed to convert response of type [" +
								content.getClass().getName() + "] to accepted type [" + acceptType + "]");
					}
					return;
				}
			}
		}
		throw new MessagingException("Could not convert reply: no suitable HttpMessageConverter found for type [" +
				content.getClass().getName() + "] and accept types [" + request.getHeaders().getAccept() + "]");
	}
	
	/**
	 * Checks if the request has a readable body (not a GET, HEAD, or OPTIONS request)
	 * and a Content-Type header.
	 */
	private boolean isReadable(ServletServerHttpRequest request) {
		HttpMethod method = request.getMethod();
		if (HttpMethod.GET.equals(method) || HttpMethod.HEAD.equals(method) || HttpMethod.OPTIONS.equals(method)) {
			return false;
		}
		return request.getHeaders().getContentType() != null;
	}
	
	/**
	 * Converts a servlet request's parameterMap to a {@link MultiValueMap}.
	 */
	@SuppressWarnings("unchecked")
	private LinkedMultiValueMap<String, String> convertParameterMap(Map parameterMap) {
		LinkedMultiValueMap<String, String> convertedMap = new LinkedMultiValueMap<String, String>();
		for (Object key : parameterMap.keySet()) {
			String[] values = (String[]) parameterMap.get(key);
			for (String value : values) {
				convertedMap.add((String) key, value);
			}
		}
		return convertedMap;
	}

	@SuppressWarnings("unchecked")
	private Object generatePayloadFromRequestBody(ServletServerHttpRequest request, Class<?> expectedType) {
		MediaType contentType = request.getHeaders().getContentType();
		if (expectedType == null) {
			expectedType = ("text".equals(contentType.getType())) ? String.class : byte[].class;
		}
		for (HttpMessageConverter<?> converter : getMessageConverters()) {
			if (converter.canRead(expectedType, contentType)) {
				try {
					return converter.read((Class) expectedType, request);
				} catch (Exception ex) {
					throw new MessagingException("Could not convert request: failure occurred during conversion of expected type [" +
							expectedType.getName() + "] from content type [" + contentType + "]", ex);
				}
			}
		}
		throw new MessagingException("Could not convert request: no suitable HttpMessageConverter found for expected type [" +
				expectedType.getName() + "] and content type [" + contentType + "]");
	}

	protected List<HttpMessageConverter<?>> getMessageConverters() {
		return this.messageConverters;
	}

}
