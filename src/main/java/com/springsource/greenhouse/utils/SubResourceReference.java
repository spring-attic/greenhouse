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
 * A resource that is the child of another.
 * @author Keith Donald
 */
public final class SubResourceReference<P, C> extends ResourceReference<C> {

	private final P parentId;
	
	public SubResourceReference(P parentId, C id, String label) {
		super(id, label);
		this.parentId = parentId;
	}

	/**
	 * The parent id.
	 * Used to generate a link to the child resource, where the parent is scoping.
	 */
	public P getParentId() {
		return parentId;
	}

}
