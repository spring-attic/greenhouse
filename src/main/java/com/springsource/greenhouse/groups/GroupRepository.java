package com.springsource.greenhouse.groups;

public interface GroupRepository {

	GroupEvent findEventByMonthOfYear(String group, Integer month, Integer year);

}
