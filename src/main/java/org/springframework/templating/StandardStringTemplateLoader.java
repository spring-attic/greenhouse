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
package org.springframework.templating;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.io.Resource;

/**
 * StringTemplateLoader that caches parsed templates in a concurrent map.
 * @author Keith Donald
 */
public class StandardStringTemplateLoader implements StringTemplateLoader {

	private Map<Resource, ResourceStringTemplateFactory> templateFactories;

	public StandardStringTemplateLoader() {
		templateFactories = new ConcurrentHashMap<Resource, ResourceStringTemplateFactory>();
	}

	public StringTemplate loadStringTemplate(Resource resource) {
		ResourceStringTemplateFactory factory = templateFactories.get(resource);
		if (factory == null) {
			factory = new ResourceStringTemplateFactory(resource);
			templateFactories.put(resource, factory);			
		}
		return factory.getStringTemplate();
	}

}