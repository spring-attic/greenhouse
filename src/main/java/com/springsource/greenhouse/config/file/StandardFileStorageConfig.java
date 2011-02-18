/*
 * Copyright 2010-2011 the original author or authors.
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
package com.springsource.greenhouse.config.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.FileStorage;
import org.springframework.data.S3FileStorage;

@Configuration
@Profile("standard")
public class StandardFileStorageConfig {

	@Bean
	public FileStorage pictureStorage(@Value("#{environment['s3.accessKey']}") String accessKey,
			@Value("#{environment['s3.secretKey']}") String secretKey) {
		return new S3FileStorage(accessKey, secretKey, "images.greenhouse.springsource.org");
	}
	
}
