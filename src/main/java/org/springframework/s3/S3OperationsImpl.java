package org.springframework.s3;

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

public class S3OperationsImpl implements S3Operations {
	private AWSCredentials awsCredentials;

	public S3OperationsImpl(String accessKey, String secretAccessKey) {
		awsCredentials = new AWSCredentials(accessKey, secretAccessKey);
	}
	
	public String saveFile(String bucket, String fileName, byte[] bytes, String contentType) throws S3ServiceException {
		S3Service s3 = new RestS3Service(awsCredentials);
		S3Bucket imageBucket = s3.getBucket(bucket);
		S3Object imageObject = new S3Object(fileName);
		imageObject.setDataInputStream(new ByteArrayInputStream(bytes));
		imageObject.setContentLength(bytes.length);
		imageObject.setContentType(contentType);
		AccessControlList acl = new AccessControlList();
		acl.setOwner(imageBucket.getOwner());
		acl.grantPermission(GroupGrantee.ALL_USERS, Permission.PERMISSION_READ);
		imageObject.setAcl(acl);
		s3.putObject(imageBucket, imageObject);	
		
		return "http://" + bucket + ".s3.amazonaws.com/" + fileName;
	}
}
