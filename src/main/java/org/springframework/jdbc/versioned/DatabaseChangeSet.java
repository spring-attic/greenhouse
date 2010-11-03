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
 * A group of changes that should be applied atomically to upgrade the database from one version to another.
 * The changes are applied in the order they are added to this change set.
 * @author Keith Donald
 */
public class DatabaseChangeSet implements Comparable<DatabaseChangeSet> {

	private final Set<DatabaseChange> changes = new LinkedHashSet<DatabaseChange>();

	private final DatabaseVersion version;
	
	/**
	 * Constructs a DatabaseChangeSet that upgrades the Database to version.
	 */
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

	public int hashCode() {
		return version.hashCode();
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof DatabaseChangeSet)) {
			return false;
		}
		DatabaseChangeSet changeSet = (DatabaseChangeSet) o;
		return version.equals(changeSet.version);
	}
	
	public int compareTo(DatabaseChangeSet changeSet) {
		if (version.lessThan(changeSet.getVersion())) {
			return -1;
		} else if (version.greaterThan(changeSet.getVersion())){
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Apply the changes in this change set to upgrade the database to {@link #getVersion() version}.
	 * Should only be called if the version of the Database is less than the version of this change set.
	 * Only upgrade the Database version if all changes in this set are applied successfully.
	 * If any change fails, the entire set will be rolled back and no changes will be applied.
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