package org.springframework.templating;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

public final class ResourceStringTemplateFactory implements StringTemplateFactory {
	
	private final org.antlr.stringtemplate.StringTemplate compiledPrototype;
	
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

	private static class DelegatingStringTemplate implements StringTemplate {

		private org.antlr.stringtemplate.StringTemplate instance;

		public DelegatingStringTemplate(org.antlr.stringtemplate.StringTemplate instance) {
			this.instance = instance;
		}

		public void put(String name, Object value) {
			instance.setAttribute(name, value);
		}

		public String render() {
			return instance.toString();
		}

	}
 }
