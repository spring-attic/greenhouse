package com.springsource.greenhouse.signup;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public class GreenhouseTestUserDatabaseFactory {

	private static final FileSystemResource userSchema = new FileSystemResource("src/main/webapp/WEB-INF/database/schema-user.sql");
	
	public static EmbeddedDatabase createUserDatabase(Resource... testDataResources) {
    	EmbeddedDatabaseFactory dbFactory = new EmbeddedDatabaseFactory();
    	dbFactory.setDatabaseType(EmbeddedDatabaseType.H2);
    	ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    	populator.addScript(userSchema);
    	for (Resource testDataResource : testDataResources) {
        	populator.addScript(testDataResource);	
    	}
    	dbFactory.setDatabasePopulator(populator);
    	return dbFactory.getDatabase();
	}
}