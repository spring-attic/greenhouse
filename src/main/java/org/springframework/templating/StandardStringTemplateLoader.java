package org.springframework.templating;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.io.Resource;

public class StandardStringTemplateLoader implements StringTemplateLoader {

	private Map<Resource, ResourceStringTemplateFactory> templateFactories;

	public StandardStringTemplateLoader() {
		templateFactories = new ConcurrentHashMap<Resource, ResourceStringTemplateFactory>();
	}

	public StringTemplate getStringTemplate(Resource resource) {
		ResourceStringTemplateFactory factory = templateFactories.get(resource);
		if (factory == null) {
			factory = new ResourceStringTemplateFactory(resource);
			templateFactories.put(resource, factory);			
		}
		return factory.createStringTemplate();
	}

}
