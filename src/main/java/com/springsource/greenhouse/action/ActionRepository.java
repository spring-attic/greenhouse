package com.springsource.greenhouse.action;

import com.springsource.greenhouse.account.Account;


public interface ActionRepository {
	
	SimpleAction createSimpleAction(String type, Account account);
	
}
