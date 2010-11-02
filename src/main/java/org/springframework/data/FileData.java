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
 * Data needed to put a file into {@link FileStorage}.
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

	/**
	 * The name of the file.
	 */
	public String getName() {
		return name;
	}

	/**
	 * The file data as a byte array.
	 */
	public byte[] getBytes() {
		return bytes;
	}

	/**
	 * The file content type.
	 */
	public String getContentType() {
		return contentType;
	}
		
}