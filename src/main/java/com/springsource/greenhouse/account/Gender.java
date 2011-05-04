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
 * Male or Female.
 * Used to customize Account messages (e.g. he or she usage) and to choose the default profile picture.
 * @author Keith Donald
 */
public enum Gender {
	
	MALE ('M'), FEMALE ('F');
	
	private char code;
	
	private Gender(char code) { 
		this.code = code;
	}
	
	public char code() {
		return code;
	}

	public static Gender valueOf(char charAt) {
		if (charAt == 'M') {
			return Gender.MALE;
		} else {
			return Gender.FEMALE;
		}
	}
}
