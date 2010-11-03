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

/**
 * A StringTemplate implementation constructed from template text.
 * The text is parsed and the template is readied for rendering.
 * Instances of this object are stateful and should not be shared between threads.
 * @author Keith Donald
 */
public class LocalStringTemplate implements StringTemplate {
	
	private StringTemplate template;
	
	public LocalStringTemplate(String template) {
		this.template = new DelegatingStringTemplate(new org.antlr.stringtemplate.StringTemplate(template));
	}

	public void put(String attribute, Object value) {
		template.put(attribute, value);
	}

	public String render() {
		return template.render();
	}
		
}
