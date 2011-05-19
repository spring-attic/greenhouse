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
package com.springsource.greenhouse.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.FileStorage;
import org.springframework.data.LocalFileStorage;
import org.springframework.data.S3FileStorage;

/**
 * Greenhouse file store configuration.
 * Used to store user profile pictures.
 * In embedded mode, we use local file storage with "delete on VM exit".
 * In standard mode, we use Amazon S3's file storage service.
 * @author Keith Donald
 */
@Configuration
public class FileStoreConfig {

	/**
	 * Local.
	 * @author Keith Donald
	 */
	@Configuration
	@Profile("embedded")
	static class Embedded {

		@Inject
		private Environment environment;

		@Inject
		private ResourceLoader resourceLoader;

		@Bean
		public FileStorage pictureStorage() {
			String applicationUrl = environment.getProperty("application.url");
			LocalFileStorage pictureStorage = new LocalFileStorage(applicationUrl + "/resources/", resourceLoader.getResource("/resources/"));
			pictureStorage.setDeleteOnExit(true);
			return pictureStorage;
		}
		
	}
	
	/**
	 * S3.
	 * @author Keith Donald
	 */
	@Configuration
	@Profile("standard")
	static class Standard {

		@Inject
		private Environment environment;

		@Bean
		public FileStorage pictureStorage() {
			return new S3FileStorage(environment.getProperty("s3.accessKey"), environment.getProperty("s3.secretKey"), "images.greenhouse.springsource.org");
		}
		
	}
	
}
