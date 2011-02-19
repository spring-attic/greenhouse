/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.account;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.FileStorage;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;

/**
 * RowMapper that maps a row in a ResultSet to an Account object instance.
 * Designed to be re-used anywhere Account SQL mapping needs to be performed.
 * Encapsulates the trickiest aspects of AccountMapping: mapping the picture and profile URL fields.
 * Also capable of mapping AccountReference objects, for generating links to public user profile without exposing private user data.
 * @author Keith Donald
 */
@Component
public class AccountMapper implements RowMapper<Account> {

	/**
	 * SELECT clause for Account fields.
	 */
	public static final String SELECT_ACCOUNT = "select id, firstName, lastName, email, username, gender, pictureSet from Member";

	/**
	 * SELECT clause for AccountReference fields.
	 */
	public static final String SELECT_ACCOUNT_REFERENCE = "select id, username, firstName, lastName, gender, pictureSet from Member";

	private final PictureUrlMapper pictureUrlMapper;
	
	private final UriTemplate profileUrlTemplate;

	/**
	 * Constructs an AccountMapper.
	 * @param pictureStorage The FileStorage for profile pictures
	 * @param profileUrlTemplate The profile URL template for generating public user profile links
	 */
	@Inject
	public AccountMapper(FileStorage pictureStorage, @Value("#{environment['application.url']}/members/{profileKey}") String profileUrlTemplate) {
		this(new PictureUrlMapper(new PictureUrlFactory(pictureStorage), PictureSize.SMALL), profileUrlTemplate);
	}

	// implementing RowMapper<Account>
	
	public Account mapRow(ResultSet rs, int row) throws SQLException {
		return new Account(rs.getLong("id"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"), rs.getString("username"), pictureUrlMapper.mapRow(rs, row), profileUrlTemplate);
	}

	/**
	 * A RowMapper that maps a lighter-weight {@link ProfileReference} object instead of a full-blown {@link Account}.
	 * Designed to be used when generating links to user profiles.
	 */
	public RowMapper<ProfileReference> getReferenceMapper() {
		return referenceMapper;
	}

	/**
	 * Creates a new Account from a Person model.
	 * The Account's username is initially null and may be changed later.
	 * The Account's profile picture is initially the default picture for the Person's gender (it may also be changed later).
	 * @param accountId the assigned internal account identifier
	 * @param person the person model
	 */
	public Account newAccount(Long accountId, Person person) {
		String pictureUrl = pictureUrlMapper.defaultPictureUrl(person.getGender());
		return new Account(accountId, person.getFirstName(), person.getLastName(), person.getEmail(), null, pictureUrl, profileUrlTemplate);
	}

	// internal helpers
	
	private final RowMapper<ProfileReference> referenceMapper = new RowMapper<ProfileReference>() {
		public ProfileReference mapRow(ResultSet rs, int row) throws SQLException {
			String id = getId(rs);
			String label = rs.getString("firstName") + " " + rs.getString("lastName");
			return new ProfileReference(id, label, pictureUrlMapper.mapRow(rs, row));
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