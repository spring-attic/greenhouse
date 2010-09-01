package org.springframework.jdbc.versioned;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.sql.DataSource;

public class DatabaseChangeSet {

	private Set<DatabaseChange> changes = new LinkedHashSet<DatabaseChange>();

	private DatabaseVersion version;
	
	public DatabaseChangeSet(DatabaseVersion version) {
		this.version = version;
	}

	public void add(DatabaseChange change) {
		changes.add(change);
	}
	
	public DatabaseVersion getVersion() {
		return version;
	}
	
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