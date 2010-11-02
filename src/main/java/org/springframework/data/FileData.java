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
package org.springframework.data;

/**
 * @author Keith Donald
 */
public final class FileData {
	
	private final String name;
	
	private final byte[] bytes;
	
	private final String contentType;

	public FileData(String name, byte[] bytes, String contentType) {
		this.name = name;
		this.bytes = bytes;
		this.contentType = contentType;
	}

	public String getName() {
		return name;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public String getContentType() {
		return contentType;
	}
		
}