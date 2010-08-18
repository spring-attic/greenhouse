package com.springsource.greenhouse.badge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Transformer;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.recent.RecentActivity;

public class AwardedBadgeRecentActivityTransformer {

	@Autowired
	public AccountRepository accountRepository;
	
	@Transformer
	public RecentActivity forAwardedBadge(AwardedBadge badge) {
		Account awardedAccount = accountRepository.findById(badge.getAccountId());
		String message = awardedAccount.getFullName() + " was awarded the " + badge.getName() + " badge";
		String memberPictureUrl = "http://localhost:8080/greenhouse/resources/images/defaultProfilePicture.png";
		String imageUrl = "http://localhost:8080/greenhouse/resources/images/defaultProfilePicture.png";
		RecentActivity activity = new RecentActivity(memberPictureUrl, message, imageUrl);
		return activity;
	}

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
}
