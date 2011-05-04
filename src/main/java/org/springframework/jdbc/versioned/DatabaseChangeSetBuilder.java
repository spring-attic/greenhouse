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
package org.springframework.jdbc.versioned;

import java.io.IOException;
import java.io.LineNumberReader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.StringUtils;

/**
 * A fluent builder for constructing a DatabaseChangeSet programatically.
 * Changes are applied in the order they are {@link #addChange(Resource) added} to the change set.
 * @author Keith Donald
 */
public class DatabaseChangeSetBuilder {
	
	private final DatabaseChangeSet changeSet;
	
	/**
	 * Constructs a builder that builds a change set that upgrades the database to the version specified.
	 * @param version the target database version
	 */
	public DatabaseChangeSetBuilder(DatabaseVersion version) {
		changeSet = new DatabaseChangeSet(version);
	}

	/**
	 * Adds a change to the change set under construction that applies the SQL contained in the resource.
	 * @param resource the SQL resource
	 * @return this, for fluent call chaining
	 */
	public DatabaseChangeSetBuilder addChange(Resource resource) {
		changeSet.add(new DatabaseChange(readSql(resource)));
		return this;
	}

	/**
	 * Called at the end of construction to get the fully-built ChangeSet.
	 */
	public DatabaseChangeSet getChangeSet() {
		return changeSet;
	}

	// internal helpers

	private String readSql(Resource resource) {
		try {
			return readScript(resource);
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not read sql resource: " + resource, e);
		}		
	}
	
	// TODO this code is duplicated from ResourceDatabasePopulator as well
	private String readScript(Resource resource) throws IOException {
		EncodedResource encoded = resource instanceof EncodedResource ? (EncodedResource) resource : new EncodedResource(resource);
		LineNumberReader lnr = new LineNumberReader(encoded.getReader());
		String currentStatement = lnr.readLine();
		StringBuilder scriptBuilder = new StringBuilder();
		while (currentStatement != null) {
			if (StringUtils.hasText(currentStatement) &&
					(SQL_COMMENT_PREFIX != null && !currentStatement.startsWith(SQL_COMMENT_PREFIX))) {
				if (scriptBuilder.length() > 0) {
					scriptBuilder.append('\n');
				}
				scriptBuilder.append(currentStatement);
			}
			currentStatement = lnr.readLine();
		}
		return scriptBuilder.toString();
	}
	
	private static String SQL_COMMENT_PREFIX = "--";
	
}