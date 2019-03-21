/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.activity.action;

import org.springframework.integration.annotation.Gateway;

/**
 * Spring Integration Message Gateway for sending action performed Messages.
 * @author Keith Donald
 */
public interface ActionGateway {
	
	/**
	 * Called by the producer of an Action to send an action performed message.
	 * Subscribers attached to the action channel can then process the action notification.
	 * Decouples the message sender from the messaging infrastructure.
	 * Generally called after Action details are recorded in the system of record. 
	 */
	@Gateway
	void actionPerformed(Action action);
	
}
