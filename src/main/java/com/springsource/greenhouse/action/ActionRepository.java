package com.springsource.greenhouse.action;


public interface ActionRepository {
	
	SimpleAction createSimpleAction(String type, org.springframework.social.account.Account account);
	
}
