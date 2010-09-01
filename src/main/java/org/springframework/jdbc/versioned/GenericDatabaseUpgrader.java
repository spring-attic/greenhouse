package org.springframework.jdbc.versioned;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericDatabaseUpgrader implements DatabaseUpgrader {
	
	private static Logger logger = LoggerFactory.getLogger(GenericDatabaseUpgrader.class);
	
	private DataSource dataSource;
	
	private Set<DatabaseChangeSet> changeSets = new LinkedHashSet<DatabaseChangeSet>();
	
	private DatabaseVersion currentVersion;
	
	public GenericDatabaseUpgrader(DataSource dataSource) {
		this.dataSource = dataSource;
		currentVersion = findCurrentVersion();
		if (logger.isInfoEnabled()) {
			logger.info("Database is at Version " + currentVersion);
		}
	}

	public void addChangeSet(DatabaseChangeSet changeSet) {
		changeSets.add(changeSet);
	}
	
	public DatabaseVersion getCurrentDatabaseVersion() {
		return currentVersion;
	}

	public void run() {
		for (DatabaseChangeSet changeSet : changeSets) {
			if (currentVersion.lessThan(changeSet.getVersion())) {
				if (logger.isInfoEnabled()) {
					logger.info("Upgrading Database to Version " + changeSet.getVersion());
				}
				currentVersion = changeSet.apply(dataSource);
				if (logger.isInfoEnabled()) {
					logger.info("Database is at Version " + currentVersion);
				}				
			}
		}
	}

	private DatabaseVersion findCurrentVersion() {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			DatabaseMetaData metadata = connection.getMetaData();
			ResultSet result = metadata.getTables(null, null, "DATABASEVERSION", null);
			try {
				if (result.next()) {
					Statement stmt = connection.createStatement();
					try {
						stmt.execute("select value from DatabaseVersion");
						ResultSet queryResult = stmt.getResultSet();
						try {
							queryResult.next();
							String value = queryResult.getString("value");
							queryResult.close();
							return DatabaseVersion.valueOf(value);
						} finally {
							queryResult.close();
						}
					} finally {
						stmt.close();
					}
				} else {
					Statement stmt = connection.createStatement();
					try {
						stmt.execute("create table DatabaseVersion (value varchar not null)");
						stmt.execute("insert into DatabaseVersion (value) values ('0.0')");
					} finally {
						stmt.close();
					}
					return DatabaseVersion.zero();
				}
			} finally {
				result.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException("Unable to determine database version", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					
				}
			}
		}
	}
		
}
