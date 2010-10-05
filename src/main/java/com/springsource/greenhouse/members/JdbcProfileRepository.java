package com.springsource.greenhouse.members;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.data.FileStorage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.social.core.SocialProviderOperations;
import org.springframework.stereotype.Service;

import com.springsource.greenhouse.account.PictureSize;
import com.springsource.greenhouse.account.PictureUrlFactory;
import com.springsource.greenhouse.account.PictureUrlMapper;
import com.springsource.greenhouse.connect.AccountProvider;

@Service
public class JdbcProfileRepository implements ProfileRepository {

	private final JdbcTemplate jdbcTemplate;

	private final PictureUrlFactory pictureUrlFactory;

	private final RowMapper<Profile> profileMapper;

	private final Map<String, AccountProvider<?>> accountProviders;

	@Inject
	public JdbcProfileRepository(JdbcTemplate jdbcTemplate, FileStorage pictureStorage,
			List<AccountProvider<?>> accountProviders) {
		this.jdbcTemplate = jdbcTemplate;
		this.pictureUrlFactory = new PictureUrlFactory(pictureStorage);
		this.profileMapper = new ProfileMapper();

		this.accountProviders = new HashMap<String, AccountProvider<?>>();
		for (AccountProvider<?> accountProvider : accountProviders) {
			this.accountProviders.put(accountProvider.getName(), accountProvider);
		}
	}
	
	public Profile findByKey(String profileKey) {
		Long accountId = getAccountId(profileKey);
		return accountId != null ? findByAccountId(accountId) : findByUsername(profileKey);
	}
	
	public Profile findByAccountId(Long accountId) {
		return jdbcTemplate.queryForObject(SELECT_PROFILE + " where id = ?", profileMapper, accountId);
	}

	public List<ConnectedProfile> findConnectedProfiles(final Long accountId) {
		return jdbcTemplate.query(SELECT_ACCOUNT_CONNECTIONS, new RowMapper<ConnectedProfile>() {
			public ConnectedProfile mapRow(ResultSet rs, int row) throws SQLException {
				String provider = rs.getString("name");
				String providerDisplayName = rs.getString("displayName");
				return new ConnectedProfile(providerDisplayName, getProfileUrl(provider, accountId));
			}
		}, accountId);
	}

	public String findProfilePictureUrl(String profileKey, PictureSize size) {
		Long accountId = getAccountId(profileKey);
		PictureUrlMapper pictureUrlMapper = new PictureUrlMapper(pictureUrlFactory, size);
		if (accountId != null) {
			return jdbcTemplate.queryForObject(SELECT_PROFILE_PIC + " where id = ?", String.class, pictureUrlMapper, accountId);
		} else {
			return jdbcTemplate.queryForObject(SELECT_PROFILE_PIC + " where username = ?", String.class, pictureUrlMapper, accountId);			
		}
	}
	
	// internal helpers
	
	private Profile findByUsername(String username) {
		return jdbcTemplate.queryForObject(SELECT_PROFILE + " where username = ?", profileMapper, username);
	}
	
	private class ProfileMapper implements RowMapper<Profile> {
		
		private PictureUrlMapper profilePictureUrlMapper = new PictureUrlMapper(pictureUrlFactory, PictureSize.large);
		
		public Profile mapRow(ResultSet rs, int row) throws SQLException {
			return new Profile(rs.getLong("id"), rs.getString("displayName"), profilePictureUrlMapper.mapRow(rs, row));
		}
	};
	
	private Long getAccountId(String id) {
		try {
			return Long.parseLong(id);
		} catch (NumberFormatException e) {
			return null;
		}		
	}

	private String getProfileUrl(String provider, Long accountId) {
		Object api = accountProviders.get(provider).getApi(accountId);
		if (api instanceof SocialProviderOperations) {
			return ((SocialProviderOperations) api).getProfileUrl();
		}
		return null;
	}

	private static final String SELECT_PROFILE = "select id, (firstName || ' ' || lastName) as displayName, gender, pictureSet from Member";

	private static final String SELECT_PROFILE_PIC = "select id, gender, pictureSet from Member";

	private static final String SELECT_ACCOUNT_CONNECTIONS = "select name, displayName, accountId from AccountConnection, AccountProvider where member = ? and provider = name order by displayName";

}