package com.springsource.greenhouse.account;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.data.FileStorage;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.util.UriTemplate;

public class AccountMapper implements RowMapper<Account> {

	public static final String SELECT_ACCOUNT = "select id, firstName, lastName, email, username, gender, pictureSet from Member";

	public static final String SELECT_ACCOUNT_REFERENCE = "select id, username, firstName, lastName, gender, pictureSet from Member";

	private final PictureUrlMapper pictureUrlMapper;
	
	private final UriTemplate profileUrlTemplate;

	@Inject
	public AccountMapper(FileStorage pictureStorage, String profileUrlTemplate) {
		this(new PictureUrlMapper(new PictureUrlFactory(pictureStorage), PictureSize.small), profileUrlTemplate);
	}

	public Account mapRow(ResultSet rs, int row) throws SQLException {
		return new Account(rs.getLong("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), pictureUrlMapper.mapRow(rs, row), profileUrlTemplate);
	}

	public RowMapper<AccountReference> getReferenceMapper() {
		return referenceMapper;
	}

	public Account newAccount(Long accountId, Person person) {
		String pictureUrl = pictureUrlMapper.defaultPictureUrl(person.getGender());
		return new Account(accountId, person.getFirstName(), person.getLastName(), person.getEmail(), null, pictureUrl, profileUrlTemplate);
	}

	// internal helpers
	
	private final RowMapper<AccountReference> referenceMapper = new RowMapper<AccountReference>() {
		public AccountReference mapRow(ResultSet rs, int row) throws SQLException {
			String id = getId(rs);
			String label = rs.getString("firstName") + " " + rs.getString("lastName");
			return new AccountReference(id, label, pictureUrlMapper.mapRow(rs, row));
		}
		
		private String getId(ResultSet rs) throws SQLException {
			String username = rs.getString("username");
			return username != null ? username : rs.getString("id");
		}
	};

	private AccountMapper(PictureUrlMapper pictureUrlMapper, String profileUrlTemplate) {
		this.pictureUrlMapper = pictureUrlMapper;
		this.profileUrlTemplate = new UriTemplate(profileUrlTemplate);
	}

}