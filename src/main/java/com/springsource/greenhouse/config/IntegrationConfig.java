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

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Imports the Spring Integration pipelines defined in the Greenhouse application.
 * This includes:
 * <ul>
 * <li>A "signup" pipeline for sending welcome emails to new users</li>
 * <li>An "activity" pipeline for sending recent activity notifications and awarding badges.</li>
 * </ul>
 * Spring Integration pipelines are components best defined in XML so there's very little here other than some imports.
 * @author Keith Donald
 */
@Configuration
@ImportResource({
	"classpath:com/springsource/greenhouse/activity/integration-activity.xml",
	"classpath:com/springsource/greenhouse/signup/integration-signup.xml" }
)
public class IntegrationConfig {

}
