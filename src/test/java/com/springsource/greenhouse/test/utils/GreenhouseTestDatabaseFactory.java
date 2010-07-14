package com.springsource.greenhouse.test.utils;

import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

public class GreenhouseTestDatabaseFactory {
	public static EmbeddedDatabase createTestDatabase(Resource... testDataResources) {
    	EmbeddedDatabaseFactory dbFactory = new EmbeddedDatabaseFactory();
    	dbFactory.setDatabaseType(EmbeddedDatabaseType.H2);
    	ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    	for (Resource testDataResource : testDataResources) {
        	populator.addScript(testDataResource);	
    	}
    	dbFactory.setDatabasePopulator(populator);
    	return dbFactory.getDatabase();
	}
}