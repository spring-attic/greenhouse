package com.springsource.greenhouse.groups;

public interface GroupRepository {
	Group findGroupByProfileKey(String profileKey);
}
