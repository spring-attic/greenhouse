package org.springframework.jdbc.versioned;

import java.io.IOException;
import java.io.LineNumberReader;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.StringUtils;

public class DatabaseChangeSetBuilder {
	
	private DatabaseChangeSet changeSet;
	
	public DatabaseChangeSetBuilder(DatabaseVersion version) {
		changeSet = new DatabaseChangeSet(version);
	}

	public void addChange(Resource resource) {
		changeSet.add(new DatabaseChange(readSql(resource)));
	}

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