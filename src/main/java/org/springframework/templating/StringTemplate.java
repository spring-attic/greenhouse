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
 * Template that generates a text String from a model.
 * @author Keith Donald
 */
public interface StringTemplate {
	
	/**
	 * Put a attribute in the model for this template.
	 */
	void put(String attribute, Object value);

	/**
	 * Generate the final text by rendering this template.
	 */
	String render();
	
}