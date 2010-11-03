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

/**
 * A Database version.
 * Currently the version is simply a major int id that should be incremented for each change set applied to the Database.
 * TODO in the future, we should consider supporting minor/micro/qualifier parts.
 * @author Keith Donald
 */
public class DatabaseVersion {

	private final int major;
	
	/**
	 * Parse a DatabaseVersion from a String.
	 */
	public static DatabaseVersion valueOf(String string) {
		return new DatabaseVersion(Integer.valueOf(string));
	}

	/**
	 * The zero database version indicating no database has been installed.
	 */
	public static DatabaseVersion zero() {
		return new DatabaseVersion(0);
	}

	public boolean equals(Object o) {
		if (!(o instanceof DatabaseVersion)) {
			return false;
		}
		DatabaseVersion version = (DatabaseVersion) o;
		return major == version.major;
	}
	
	public int hashCode() {
		return major * 29;
	}

	/**
	 * True if this DatabaseVersion is less than the version provided.
	 */
	public boolean lessThan(DatabaseVersion version) {
		return major < version.major;
	}

	/**
	 * True if this DatabaseVersion is greater than the version provided.
	 */
	public boolean greaterThan(DatabaseVersion version) {
		return major > version.major;
	}
	
	private DatabaseVersion(int major) {
		this.major = major;
	}
	
	public String toString() {
		return String.valueOf(major);
	}

}
