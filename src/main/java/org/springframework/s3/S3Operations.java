package org.springframework.s3;

import org.jets3t.service.S3ServiceException;

public interface S3Operations {
	
	// TODO this currently works with a bogus bucket name - investigate
	void saveFile(String bucket, String fileName, byte[] bytes, String contentType) throws S3ServiceException;

}