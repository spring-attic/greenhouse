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
 * A message to display to the user.
 * Has a type indicating the kind of message.
 * @author Keith Donald
 */
public final class Message {
	
	private final MessageType type;
	
	private final String text;

	/**
	 * Creates a new Message of a certain type consisting of the text provided.
	 */
	public Message(MessageType type, String text) {
		this.type = type;
		this.text = text;
	}

	/**
	 * Factory method for a success message.
	 */
	public static Message success(String text) {
		return new Message(MessageType.SUCCESS, text);
	}

	/**
	 * Factory method for an info message.
	 */
	public static Message info(String text) {
		return new Message(MessageType.INFO, text);
	}

	/**
	 * Factory method for a warning message.
	 */
	public static Message warning(String text) {
		return new Message(MessageType.WARNING, text);
	}

	/**
	 * Factory method for an error message.
	 */
	public static Message error(String text) {
		return new Message(MessageType.ERROR, text);
	}

	/**
	 * The type of message; such as info, warning, error, or success.
	 */
	public MessageType getType() {
		return type;
	}

	/**
	 * The info text.
	 */
	public String getText() {
		return text;
	}
	
	public String toString() {
		return type + ": " + text;
	}

}