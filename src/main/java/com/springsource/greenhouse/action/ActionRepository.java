package com.springsource.greenhouse.action;

public interface ActionRepository {
	
	SimpleAction createSimpleAction(String type, Long accountId, Location location);
	
}
