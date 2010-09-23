package com.springsource.greenhouse.account;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.FileStorage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.encrypt.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.utils.EmailUtils;

@Repository
public class JdbcAccountRepository implements AccountRepository {

	private final JdbcTemplate jdbcTemplate;

	private final PasswordEncoder passwordEncoder;

	private final PictureUrlFactory pictureUrlFactory;

	private final UriTemplate profileUrlTemplate;

	private final AccountMapper accountMapper;

	@Autowired
	public JdbcAccountRepository(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder, FileStorage pictureStorage, String profileUrlTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.passwordEncoder = passwordEncoder;
		this.pictureUrlFactory = new PictureUrlFactory(pictureStorage);
		this.profileUrlTemplate = new UriTemplate(profileUrlTemplate);
		this.accountMapper = new AccountMapper(new PictureUrlMapper(pictureUrlFactory, PictureSize.small), this.profileUrlTemplate);
	}

	@Transactional
	public Account createAccount(Person person) throws EmailAlreadyOnFileException {
		try {
			jdbcTemplate.update("insert into Member (firstName, lastName, email, password, gender, birthdate) values (?, ?, ?, ?, ?, ?)",
				person.getFirstName(), person.getLastName(), person.getEmail(), passwordEncoder.encode(person.getPassword()), person.getGender().code(), person.getBirthdate().toString());
			Long accountId = jdbcTemplate.queryForLong("call identity()");
			String pictureUrl = pictureUrlFactory.defaultPictureUrl(person.getGender(), PictureSize.small);
			return new Account(accountId, person.getFirstName(), person.getLastName(), person.getEmail(), null, pictureUrl, profileUrlTemplate);
		} catch (DuplicateKeyException e) {
			throw new EmailAlreadyOnFileException(person.getEmail());
		}
	}

	public Account authenticate(String username, String password) throws UsernameNotFoundException,
			InvalidPasswordException {
		try {
			return jdbcTemplate.queryForObject(passwordProtectedAccountQuery(username), passwordProtectedAccountMapper, username).accessAccount(password, passwordEncoder);
		} catch (EmptyResultDataAccessException e) {
			throw new UsernameNotFoundException(username);
		}
	}

	public void changePassword(Long accountId, String password) {
		jdbcTemplate.update("update Member set password = ? where id = ?", passwordEncoder.encode(password), accountId);
	}

	public Account findById(Long id) {
		return jdbcTemplate.queryForObject(SELECT_ACCOUNT + " where id = ?", accountMapper, id);
	}

	public Account findByUsername(String username) throws UsernameNotFoundException {
		try {
			return jdbcTemplate.queryForObject(accountQuery(username), accountMapper, username);
		} catch (EmptyResultDataAccessException e) {
			throw new UsernameNotFoundException(username);
		}
	}

	public void markProfilePictureSet(Long id) {
		jdbcTemplate.update("update Member set pictureSet = true where id = ?", id);
	}

	public Account findByAccountConnection(String provider, String accessToken) throws InvalidAccessTokenException {
		try {
			return jdbcTemplate.queryForObject(SELECT_ACCOUNT + " where id = (select member from AccountConnection where provider = ? and accessToken = ?)",
					accountMapper, provider, accessToken);
		} catch (EmptyResultDataAccessException e) {
			throw new InvalidAccessTokenException(accessToken);
		}
	}

	// internal helpers

	private String accountQuery(String username) {
		return EmailUtils.isEmail(username) ? SELECT_ACCOUNT + " where email = ?" : SELECT_ACCOUNT + " where username = ?";
	}

	private String passwordProtectedAccountQuery(String username) {
		return EmailUtils.isEmail(username) ? SELECT_PASSWORD_PROTECTED_ACCOUNT + " where email = ?" : SELECT_PASSWORD_PROTECTED_ACCOUNT + " where username = ?";
	}

	private RowMapper<PasswordProtectedAccount> passwordProtectedAccountMapper = new RowMapper<PasswordProtectedAccount>() {
		public PasswordProtectedAccount mapRow(ResultSet rs, int row) throws SQLException {
			Account account = accountMapper.mapRow(rs, row);
			return new PasswordProtectedAccount(account, rs.getString("password"));
		}
	};

	private static class PasswordProtectedAccount {

		private Account account;

		private String encodedPassword;

		public PasswordProtectedAccount(Account account, String encodedPassword) {
			this.account = account;
			this.encodedPassword = encodedPassword;
		}

		public Account accessAccount(String password, PasswordEncoder passwordEncoder) throws InvalidPasswordException {
			if (passwordEncoder.matches(password, encodedPassword)) {
				return account;
			} else {
				throw new InvalidPasswordException();
			}
		}

	}

	private static final String SELECT_ACCOUNT = "select id, firstName, lastName, email, username, gender, pictureSet from Member";

	private static final String SELECT_PASSWORD_PROTECTED_ACCOUNT = "select id, firstName, lastName, email, password, username, gender, pictureSet from Member";
}