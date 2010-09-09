package org.springframework.data;

public interface FileStorage {

	String absoluteUrl(String fileName);

	// TODO exception hierarchy
	String storeFile(FileData file);
	
}
