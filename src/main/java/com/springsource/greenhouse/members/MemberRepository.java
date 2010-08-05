package com.springsource.greenhouse.members;

import java.util.Map;

public interface MemberRepository {

	Member findMemberByProfileKey(String profileKey);

	Member findMemberByAccountId(Long accountId);

	Member findMemberByUsername(String username);

	Map<String, String> lookupConnectedAccountIds(String profileKey);
}