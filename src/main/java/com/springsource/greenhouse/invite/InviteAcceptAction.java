package com.springsource.greenhouse.invite;

import org.joda.time.DateTime;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.activity.action.Action;
import com.springsource.greenhouse.utils.Location;

public final class InviteAcceptAction extends Action {

	private final Long sentBy;
	
	private final DateTime sentTime;
	
	public InviteAcceptAction(Long id, DateTime time, Account account, Location location, Long sentBy, DateTime sentTime) {
		super(id, time, account, location);
		this.sentBy = sentBy;
		this.sentTime = sentTime;
	}
	
	public Long getSentBy() {
		return sentBy;
	}
	
	public DateTime getSentTime() {
		return sentTime;
	}
	
	public String toString() {
		// TODO Account.gender needs to be preserved
		return getAccount().getFullName() + " accepted his Greenhouse invitation";
	}
	
}
