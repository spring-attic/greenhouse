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
package org.springframework.web.flash;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Map for messages that should survive a redirect.
 * @author Keith Donald
 */
public final class FlashMap {
	
	static final String FLASH_MAP_SESSION_ATTRIBUTE = FlashMap.class.getName();
	
	/**
	 * Get the Flash Map for the current user session.
	 * Creates one if necessary.
	 * Note this method will create a HttpSession if one does not already exist.
	 * @param request the servlet request
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getCurrent(HttpServletRequest request) {
		HttpSession session = request.getSession();
		synchronized (session) {
			Map<String, Object> flash = (Map<String, Object>) session.getAttribute(FLASH_MAP_SESSION_ATTRIBUTE);
			if (flash == null) {
				flash = new HashMap<String, Object>();
				session.setAttribute(FLASH_MAP_SESSION_ATTRIBUTE, flash);
			}
			return flash;
		}
	}
	
	/**
	 * Put an attribute in the current flash map.
	 * @param name the attribute name
	 * @param value the attribute value
	 */
	public static void put(String name, Object value) {
		getCurrent(getRequest(RequestContextHolder.currentRequestAttributes())).put(name, value);
	}

	/**
	 * Set the 'message' attribute to a info {@link Message} that renders the info text. 
	 */
	public static void setInfoMessage(String info) {
		put(MESSAGE_ATTRIBUTE, new Message(MessageType.iNFO, info));
	}

	/**
	 * Set the 'message' attribute to a warning {@link Message} that renders the warning text. 
	 */
	public static void setWarningMessage(String warning) {
		put(MESSAGE_ATTRIBUTE, new Message(MessageType.WARNING, warning));
	}

	/**
	 * Set the 'message' attribute to a error {@link Message} that renders the error text. 
	 */
	public static void setErrorMessage(String error) {
		put(MESSAGE_ATTRIBUTE, new Message(MessageType.ERROR, error));
	}

	/**
	 * Set the 'message' attribute to a success {@link Message} that renders the success text. 
	 */
	public static void setSuccessMessage(String success) {
		put(MESSAGE_ATTRIBUTE, new Message(MessageType.SUCCESS, success));
	}

	/**
	 * A message to display to the user.
	 * Has a type indicating the kind of message.
	 * @author Keith Donald
	 */
	public static final class Message {
		
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
	
	/**
	 * Enumeration of Message types.
	 * @author Keith Donald
	 */
	public static enum MessageType {
		iNFO, SUCCESS, WARNING, ERROR
	}

	private static HttpServletRequest getRequest(RequestAttributes requestAttributes) {
		return ((ServletRequestAttributes)requestAttributes).getRequest();
	}

	private static final String MESSAGE_ATTRIBUTE = "message";

	private FlashMap() {
	}

}