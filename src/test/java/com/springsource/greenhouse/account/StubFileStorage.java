package com.springsource.greenhouse.account;

import org.springframework.data.FileData;
import org.springframework.data.FileStorage;

public class StubFileStorage implements FileStorage {

	public String absoluteUrl(String fileName) {
		return "http://localhost:8080/resources/" + fileName;
	}
	
	public String storeFile(FileData file) {
		return absoluteUrl(file.getContentType());
	}
	
}