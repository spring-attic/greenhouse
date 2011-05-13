/*
 * Copyright 2010-2011 the original author or authors.
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

import javax.inject.Inject;

import org.springframework.core.env.Environment;

/**
 * Base class that simply exposes a protected field referencing the current Environment.
 * Allows subclasses to access environment properties when constructing beans.
 * @author Keith Donald
 * @see Environment
 */
abstract class EnvironmentAwareConfig {
	
	/**
	 * The current environment.
	 */
	@Inject
	protected Environment environment;
	
}
