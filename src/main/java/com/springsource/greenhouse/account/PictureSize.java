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
package com.springsource.greenhouse.account;

/**
 * Enum constants for the various member profile picture sizes.
 * @author Keith Donald
 */
public enum PictureSize {
	
	/**
	 * The small picture size, used for profile link thumbnails.
	 */
	SMALL,
	
	/**
	 * The normal or medium picture size, used in the default case.
	 */
	NORMAL,
	
	/**
	 * The large picture size, used when viewing a detailed profile of the member.
	 */
	LARGE
}