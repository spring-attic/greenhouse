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
 * Service interface for a Database migrator or upgrader.
 * @author Keith Donald
 */
public interface DatabaseUpgrader {

	/**
	 * The current version of the Database.
	 * Returns {@link DatabaseVersion#zero()} if no database has been installed.
	 */
	public DatabaseVersion getCurrentDatabaseVersion();
	
	/**
	 * Run this Database upgrader.
	 * The upgrader will upgrade the database to the latest version.
	 * It will first determine the current version of the Database, then apply the change sets needed to upgrade the Database to the latest version.
	 * If the Database is already at the latest version, no action is taken. 
	 */
	public void run();
	
}
