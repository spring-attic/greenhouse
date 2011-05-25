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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractDatabaseChange implements DatabaseChange {
	
	/**
	 * Apply this change.
	 */
	public void apply(Connection connection) {
		Statement statement = getStatement(connection);
		try {
			doExecuteStatement(statement);
		} catch (SQLException e) {
			throw new RuntimeException("Failed to apply", e);
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
			}
		}
	}
	
	private Statement getStatement(Connection connection) {
		try {
			return doCreateStatement(connection);
		} catch (SQLException e) {
			throw new IllegalStateException("Could not create statement", e);
		}		
	}

	protected abstract Statement doCreateStatement(Connection connection) throws SQLException;
	
	protected abstract void doExecuteStatement(Statement statement) throws SQLException;
}
