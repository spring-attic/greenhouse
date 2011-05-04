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
package com.springsource.greenhouse.members;

import java.io.IOException;

/**
 * A service for storing profile pictures.
 * @author Keith Donald
 * @author Craig Walls
 */
public interface ProfilePictureService {

	/**
	 * Save the imageBytes as the profile picture for the member account.
	 * The bytes are generally converted to JPG format for space, then scaled to produce multiple picture dimensions, such as a small, normal, and large.
	 * @param accountId the member account id
	 * @param imageBytes the image bytes
	 * @throws IOException failed to store the image due to an output failure
	 */
	void saveProfilePicture(Long accountId, byte[] imageBytes) throws IOException;
	
}