package org.springframework.social.facebook;

import static java.util.Arrays.*;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class FacebookService implements FacebookOperations {
	private static final String GET_CURRENT_USER_INFO = "https://graph.facebook.com/me?access_token={token}";
	
	private RestTemplate restTemplate;
	
	public FacebookService() {
		MappingJacksonHttpMessageConverter jsonMessageConverter = new MappingJacksonHttpMessageConverter();
		jsonMessageConverter.setSupportedMediaTypes(asList(new MediaType("text", "javascript")) );
		this.restTemplate = new RestTemplate();
		this.restTemplate.getMessageConverters().add(jsonMessageConverter);
	}

	public FacebookUserInfo getUserInfo(String accessToken) {
		return restTemplate.getForObject(GET_CURRENT_USER_INFO, FacebookUserInfo.class, accessToken);
    }

}
