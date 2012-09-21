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
package com.springsource.greenhouse.signup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import com.springsource.greenhouse.account.Gender;

class SignupFormDeserializers {

	static final class BirthdateDeserializer extends JsonDeserializer<List<Integer>> {
		@Override
		public List<Integer> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			JsonNode tree = jp.readValueAsTree();
			List<Integer> fields = new ArrayList<Integer>();
			fields.add(tree.get("month").getIntValue());
			fields.add(tree.get("day").getIntValue());
			fields.add(tree.get("year").getIntValue());
			return fields;
		}
	}

	static final class GenderDeserializer extends JsonDeserializer<Gender> {
		@Override
		public Gender deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			return Gender.valueOf(jp.getText().toUpperCase().charAt(0));
		}
	}
	
}
