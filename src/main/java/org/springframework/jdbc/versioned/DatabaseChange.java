package org.springframework.jdbc.versioned;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseChange {
	
	private String sql;
	
	public DatabaseChange(String sql) {
		this.sql = sql;
	}

	public void apply(Connection connection) {
		Statement statement = getStatement(connection);
		try {
			statement.execute(sql);
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
			return connection.createStatement();
		} catch (SQLException e) {
			throw new IllegalStateException("Could not create statement", e);
		}		
	}
}
