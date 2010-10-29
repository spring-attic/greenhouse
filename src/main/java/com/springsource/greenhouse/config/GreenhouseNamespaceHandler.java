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
package com.springsource.greenhouse.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Registers bean-definition parsers for the custom greenhouse XML configuration namespace.
 * Currently empty, and serves as a placeholder in the event we need to add custom configuration tags here in the Greenhouse project
 * before adding them to a Spring project.
 * @author Keith Donald
 */
public class GreenhouseNamespaceHandler extends NamespaceHandlerSupport {

	public void init() {
		// TODO add custom tags here as we need them for our application
	}

}

