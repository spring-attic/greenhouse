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
 * Enumeration of Message types.
 * @author Keith Donald
 */
public enum MessageType {
	
	/**
	 * The message is informative in nature, like a note or notice.
	 */
	INFO, 

	/**
	 * The message indicates that an action initiated by the user was performed successfully.
	 */
	SUCCESS, 
	
	/**
	 * The message warns the user something is not quite right.
	 * Corrective action is generally recommended but not required.
	 */
	WARNING, 
	
	/**
	 * The message reports an error condition that needs to be addressed by the user.
	 */
	ERROR;
	
	private final String cssClass;
	
	private MessageType() {
		cssClass = name().toLowerCase(); 
	}
	
	/**
	 * The css class for styling messages of this type.
	 */
	public String getCssClass() {
		return cssClass;
	}
}