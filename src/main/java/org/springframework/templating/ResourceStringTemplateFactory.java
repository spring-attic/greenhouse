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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

/**
 * A StringTemplate factory that creates template instances from a single Resource.
 * @author Keith Donald
 */
public final class ResourceStringTemplateFactory implements StringTemplateFactory {
	
	private final org.antlr.stringtemplate.StringTemplate compiledPrototype;
	
	/**
	 * Creates a StringTemplateFactory that creates its StringTemplate instances from the resource.
	 * @param resource the resource where the template text is defined
	 */
	public ResourceStringTemplateFactory(Resource resource) {
		this.compiledPrototype = createPrototype(resource);
	}
	
	public StringTemplate getStringTemplate() {
		return new DelegatingStringTemplate(compiledPrototype.getInstanceOf());
	}
	
	// internal helpers
	
	private org.antlr.stringtemplate.StringTemplate createPrototype(Resource resource) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(getResourceReader(resource));
			return loadTemplate(getTemplateName(resource), reader);
		}
		catch (IOException e) {
			throw new IllegalArgumentException("Unable to read template resource " + resource, e);
		}
		finally {
			if (reader != null) {
				try {
					reader.close();
				}
				catch (IOException e) {
					throw new IllegalStateException("Unable to close template resource " + resource, e);
				}
			}
		}		
	}
	
	private Reader getResourceReader(Resource resource) throws IOException {
		if (resource instanceof EncodedResource) {
			return ((EncodedResource) resource).getReader();
		} else {
			return new EncodedResource(resource).getReader();
		}
	}
	
	private String getTemplateName(Resource resource) {
		return resource.getFilename();
	}
	
	private org.antlr.stringtemplate.StringTemplate loadTemplate(String name, BufferedReader reader) throws IOException {
		String line;
		String nl = System.getProperty("line.separator");
		StringBuffer buf = new StringBuffer(300);
		while ((line = reader.readLine()) != null) {
			buf.append(line);
			buf.append(nl);
		}
		String pattern = buf.toString().trim();
		if (pattern.length() == 0) {
			return null;
		}
		org.antlr.stringtemplate.StringTemplate template = new org.antlr.stringtemplate.StringTemplate(pattern);
		template.setName(name);
		return template;
	}
 }
