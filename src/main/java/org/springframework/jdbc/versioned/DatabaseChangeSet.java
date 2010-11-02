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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.sql.DataSource;

/**
 * A group of changes that are applied atomatically that upgrade the database from one version to another.
 * @author Keith Donald
 */
public class DatabaseChangeSet {

	private Set<DatabaseChange> changes = new LinkedHashSet<DatabaseChange>();

	private final DatabaseVersion version;
	
	public DatabaseChangeSet(DatabaseVersion version) {
		this.version = version;
	}

	/**
	 * Add a change to this change set.
	 */
	public void add(DatabaseChange change) {
		changes.add(change);
	}
	
	/**
	 * The version the Database will be at after applying this change set.
	 */
	public DatabaseVersion getVersion() {
		return version;
	}
	
	/**
	 * Apply the changes in this change set to upgrade the database to {@link #getVersion()}.
	 */
	public DatabaseVersion apply(DataSource dataSource) {
		Connection connection = getTransactionalConnection(dataSource);
		try {
			try {
				for (DatabaseChange change : changes) {
					change.apply(connection);
				}
				updateDatabaseVersion(version, connection);
			} catch (RuntimeException e) {
				rollbackTransaction(connection);
				throw e;
			}
			commitTransaction(connection);
			return version;			
		} finally {
			closeConnection(connection);
		}
	}
	
	// internal helpers
	
	private Connection getTransactionalConnection(DataSource dataSource) {
		try {
			Connection connection = dataSource.getConnection();
			beginTransaction(connection);
			return connection;
		}
		catch (SQLException e) {
			throw new RuntimeException("Could not get JDBC Connection", e);
		}
	}
	
	private void beginTransaction(Connection connection) {
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			throw new RuntimeException("Could not begin database transaction", e);
		}
	}
	
	private void updateDatabaseVersion(DatabaseVersion version, Connection connection) {
		try {
			PreparedStatement statement = connection.prepareStatement("update DatabaseVersion set value = ?");
			statement.setString(1, version.toString());
			statement.execute();
		} catch (SQLException e) {
			throw new RuntimeException("Failed to update database version", e);
		}		
	}
	
	private void commitTransaction(Connection connection) {
		try {
			connection.commit();
		} catch (SQLException e) {
			throw new RuntimeException("Could not commit", e);
		}		
	}

	private void rollbackTransaction(Connection connection) {
		try {
			connection.rollback();
		} catch (SQLException e) {
			throw new RuntimeException("Could not rollback", e);
		}
	}
	
	private void closeConnection(Connection connection) {
		try {
			connection.close();
		}
		catch (SQLException ex) {
			// ignore
		}		
	}
	
}