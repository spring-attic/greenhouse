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

import java.io.ByteArrayInputStream;

import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.acl.GroupGrantee;
import org.jets3t.service.acl.Permission;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

/**
 * Amazon S3-based file storage.
 * Allows files to be stored in S3.
 * @author Keith Donald
 * @author Craig Walls
 */
public class S3FileStorage implements FileStorage {
	
	private final AWSCredentials awsCredentials;

	private final String bucketName;
	
	/**
	 * Creates a S3-based file storage.
	 * @param accessKey the S3 access key
	 * @param secretAccessKey S3 the secret
	 * @param bucketName the bucket in your account where files should be stored
	 */
	public S3FileStorage(String accessKey, String secretAccessKey, String bucketName) {
		awsCredentials = new AWSCredentials(accessKey, secretAccessKey);
		this.bucketName = bucketName;
	}
	
	public String absoluteUrl(String fileName) {
		return "http://" + bucketName + "/" + fileName;
	}

	public String storeFile(FileData file) {
		S3Service s3 = createS3Service();
		S3Bucket bucket;
		try {
			bucket = s3.getBucket(bucketName);
		} catch (S3ServiceException e) {
			throw new IllegalStateException("Unable to retrieve S3 Bucket", e);
		}
		S3Object object = new S3Object(file.getName());
		object.setDataInputStream(new ByteArrayInputStream(file.getBytes()));
		object.setContentLength(file.getBytes().length);
		object.setContentType(file.getContentType());		
		AccessControlList acl = new AccessControlList();
		acl.setOwner(bucket.getOwner());
		acl.grantPermission(GroupGrantee.ALL_USERS, Permission.PERMISSION_READ);
		object.setAcl(acl);
		try {
			s3.putObject(bucket, object);
		} catch (S3ServiceException e) {
			throw new RuntimeException("Unable to put object into S3", e);
		}
		return absoluteUrl(file.getName());
	}
	
	// internal helpers
	
	private S3Service createS3Service() {
		try {
			return new RestS3Service(awsCredentials);
		} catch (S3ServiceException e) {
			throw new IllegalArgumentException("Unable to init REST-based S3Service with provided credentials", e);
		}
	}
	
}
