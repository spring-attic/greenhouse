package com.springsource.greenhouse.groups;

public interface GroupRepository {
	Group findGroupBySlug(String slug);
}
