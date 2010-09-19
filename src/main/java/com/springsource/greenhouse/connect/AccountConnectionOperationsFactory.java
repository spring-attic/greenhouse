package com.springsource.greenhouse.connect;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestOperations;

import com.springsource.greenhouse.account.Account;

public class AccountConnectionOperationsFactory {
	public static RestOperations getProviderOperations(JdbcTemplate jdbcTemplate, String providerName) {
		AccountProviderRepository providerRepo = new JdbcAccountProviderRepository(jdbcTemplate);
		AccountProvider provider = providerRepo.findAccountProviderByName(providerName);
		return provider.getAccountConnectionOperations(getAccountId());
	}

	private static Long getAccountId() {
		Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return account.getId();
	}
}
