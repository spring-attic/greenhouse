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
package com.springsource.greenhouse.utils;

/**
 * A reference to another REST-ful resource.
 * @author Keith Donald
 */
public class ResourceReference<T> {
	
	private final T id;
	
	private final String label;

	public ResourceReference(T id, String label) {
		this.id = id;
		this.label = label;
	}

	/**
	 * The id of the refernced entity.
	 * Used to generate a hyperlink to the resource.
	 */
	public T getId() {
		return id;
	}

	/**
	 * A caption or label for the entity.
	 */
	public String getLabel() {
		return label;
	}
	
}
