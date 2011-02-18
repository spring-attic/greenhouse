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

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.FileStorage;
import org.springframework.data.LocalFileStorage;

@Configuration
@Profile("embedded")
public class EmbeddedFileStorageConfig {

	@Inject
	private ResourceLoader resourceLoader;
	
	@Bean
	public FileStorage pictureStorage(@Value("#{environment['application.url']}") String applicationUrl) {
		LocalFileStorage pictureStorage = new LocalFileStorage(applicationUrl + "/resources/", resourceLoader.getResource("/resources/"));
		pictureStorage.setDeleteOnExit(true);
		return pictureStorage;
	}
	
}
