package org.springframework.s3;

import org.jets3t.service.S3ServiceException;

public interface S3Operations {

	public abstract void saveFile(String bucket, String fileName, byte[] bytes, String contentType) throws S3ServiceException;

}