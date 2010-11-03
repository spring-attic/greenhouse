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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import org.springframework.core.io.Resource;

/**
 * File storage implementation that stores files on the local file system within a configurable base directory.
 * @author Keith Donald
 */
public class LocalFileStorage implements FileStorage {

	private final String storageUrl;
	
	private final File storageDirectory;
	
	private boolean deleteOnExit;
	
	/**
	 * Constructs a local file storage.
	 * @param storageUrl the logical URL pointing to the root of this file storage
	 * @param storageDirectory the path to the base directory on the filesystem where files physically reside; created if necessary.
	 */
	public LocalFileStorage(String storageUrl, Resource storageDirectory) {
		this.storageUrl = storageUrl;
		try {
			this.storageDirectory = storageDirectory.getFile();
			this.storageDirectory.deleteOnExit();
			this.storageDirectory.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Whether the files stored in local storage should be automatically deleted when the VM shuts down.
	 * Useful for temporary file stores.  Defaults to false.
	 */
	public void setDeleteOnExit(boolean deleteOnExit) {
		this.deleteOnExit = deleteOnExit;
	}
	
	public String absoluteUrl(String fileName) {
		return storageUrl + fileName;
	}
	
	public String storeFile(FileData fileData) {
 		File file = new File(storageDirectory, fileData.getName());
 		if (file.exists()) {
 			file.delete();
 		}
		File parent = file.getParentFile();
		if (parent != null && !parent.equals(storageDirectory)) {
			parent.mkdirs();
		}
		LinkedList<File> parents = new LinkedList<File>();
 		while (parent != null && !parent.equals(storageDirectory)) {
 			parents.addFirst(parent);
			parent = parent.getParentFile();
		}
 		if (deleteOnExit) {
 			for (File p : parents) {
 				p.deleteOnExit();
 			}
 			file.deleteOnExit();
 		}
 		try {
 			file.createNewFile();
 			FileOutputStream os = new FileOutputStream(file);
			os.write(fileData.getBytes());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return file.toURI().toString();
	}
	
}