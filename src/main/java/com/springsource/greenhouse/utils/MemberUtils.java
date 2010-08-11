package com.springsource.greenhouse.utils;

import javax.servlet.http.HttpServletRequest;

import com.springsource.greenhouse.account.Account;

public class MemberUtils {
	
	public static String assembleMemberProfileUrl(HttpServletRequest request, Account account) {
		int serverPort = request.getServerPort();
		String portPart = serverPort == 80 || serverPort == 443 ? "" : ":" + serverPort;
		return request.getScheme() + "://" + request.getServerName() + portPart + request.getContextPath() + "/members/" + account.getProfileKey();
	}
	
}
