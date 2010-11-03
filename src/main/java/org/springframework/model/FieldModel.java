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
package org.springframework.model;

/**
 * A model for a form field.
 * TODO experimental.
 * @author Keith Donald
 */
public class FieldModel<T> {

	private String errorMessage;
	
	private T value;
	
	public FieldModel() {	
	}
	
	public FieldModel(String errorMessage, T value) {
		this.errorMessage = errorMessage;
		this.value = value;
	}
	
	/**
	 * True if the field value is invalid.
	 */
	public boolean isError() {
		return errorMessage != null;
	}

	/**
	 * Get the reason the field is in an invalid state.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Get the actual field value.
	 */
	public T getValue() {
		return value;
	}

	public static <T> FieldModel<T> error(String errorMessage, T value) {
		return new FieldModel<T>(errorMessage, value);
	}

}
