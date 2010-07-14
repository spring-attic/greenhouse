package com.springsource.greenhouse.members;

public interface MembersService {

	Member findMemberByProfileKey(String profileKey);

	Member findMemberByAccountId(Long accountId);

	Member findMemberByUsername(String username);

}