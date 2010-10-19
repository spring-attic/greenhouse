package org.springframework.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import org.springframework.core.io.Resource;

public class LocalFileStorage implements FileStorage {

	private final String storageUrl;
	
	private final File storageDirectory;
	
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
 		for (File p : parents) {
 			p.deleteOnExit();
 		}
		file.deleteOnExit();
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