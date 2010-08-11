package com.springsource.greenhouse.badge;

import javax.inject.Inject;

import org.springframework.beans.factory.FactoryBean;

public class BadgeSystemFactoryBean implements FactoryBean<BadgeSystem> {

	private BadgeRepository badgeRepository;
	
	@Inject
	public BadgeSystemFactoryBean(BadgeRepository badgeRepository) {
		this.badgeRepository = badgeRepository;
	}

	public Class<?> getObjectType() {
		return BadgeSystem.class;
	}
	
	public boolean isSingleton() {
		return true;
	}

	public BadgeSystem getObject() throws Exception {
		StandardBadgeSystem badgeSystem = new StandardBadgeSystem();
		badgeSystem.add(new SimpleActionTriggeredBadgeAwarder("Newbie", badgeRepository), "SignedUp");
		return badgeSystem;
	}

}