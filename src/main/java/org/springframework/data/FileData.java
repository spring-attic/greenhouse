package org.springframework.data;

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