package com.springsource.greenhouse.action;

import org.joda.time.DateTime;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.utils.Location;

public interface ActionRepository {

	SimpleAction createSimpleAction(String type, Account account);

	<A extends Action> A createAction(Class<A> actionClass, Account account, ActionFactory<A> actionFactory);

	public interface ActionFactory<A extends Action> {
		A createAction(Long id, DateTime performTime, Account account, Location location);
	}
}