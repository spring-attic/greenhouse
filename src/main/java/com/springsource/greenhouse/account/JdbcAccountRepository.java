package com.springsource.greenhouse.account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.FileStorage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.encrypt.PasswordEncoder;
import org.springframework.security.encrypt.SecureRandomStringKeyGenerator;
import org.springframework.security.encrypt.StringEncryptor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriTemplate;

import com.springsource.greenhouse.utils.EmailUtils;

@Repository
public class JdbcAccountRepository implements AccountRepository {

	private final JdbcTemplate jdbcTemplate;

	private final StringEncryptor encryptor;

	private final PasswordEncoder passwordEncoder;

	private final PictureUrlFactory pictureUrlFactory;

	private final UriTemplate profileUrlTemplate;

	private final AccountMapper accountMapper;

	private final SecureRandomStringKeyGenerator keyGenerator = new SecureRandomStringKeyGenerator();

	@Autowired
	public JdbcAccountRepository(JdbcTemplate jdbcTemplate, StringEncryptor encryptor, PasswordEncoder passwordEncoder, FileStorage pictureStorage, String profileUrlTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.encryptor = encryptor;
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

	public void connectAccount(Long id, String provider, String accessToken, String accountId)
			throws AccountConnectionAlreadyExists {
		try {
			jdbcTemplate.update(INSERT_ACCOUNT_CONNECTION, id, provider, accessToken, accountId);
		} catch (DuplicateKeyException e) {
			throw new AccountConnectionAlreadyExists(provider, accountId);
		}
	}

	public Account findByAccountConnection(String provider, String accessToken) throws InvalidAccessTokenException {
		try {
			return jdbcTemplate.queryForObject(SELECT_ACCOUNT + " where id = (select member from AccountConnection where provider = ? and accessToken = ?)",
					accountMapper, provider, accessToken);
		} catch (EmptyResultDataAccessException e) {
			throw new InvalidAccessTokenException(accessToken);
		}
	}

	public boolean hasAccountConnection(Long id, String provider) {
		return jdbcTemplate.queryForInt(SELECT_ACCOUNT_CONNECTION_COUNT, id, provider) == 1;
	}

	public void disconnectAccount(Long id, String provider) {
		jdbcTemplate.update(DELETE_ACCOUNT_CONNECTION, id, provider);
	}

	public List<Account> findFriendAccounts(String provider, List<String> friendIds) {
		NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		Map<String, Object> params = new HashMap<String, Object>(2, 1);
		params.put("provider", provider);
		params.put("friendIds", friendIds);
		return namedTemplate.query(SELECT_ACCOUNT + " where id in (select member from AccountConnection where provider = :provider and accountId in ( :friendIds )) ",
				params, accountMapper);
	}

	@Transactional
	public AppConnection connectApp(Long accountId, String apiKey) throws InvalidApiKeyException {
		String accessToken = keyGenerator.generateKey();
		String secret = keyGenerator.generateKey();
		Long appId;
		try {
			appId = jdbcTemplate.queryForLong("select id from App where apiKey = ?", encryptor.encrypt(apiKey));
		} catch (EmptyResultDataAccessException e) {
			throw new InvalidApiKeyException(apiKey);
		}
		jdbcTemplate.update("delete from AppConnection where app = ? and member = ?", appId, accountId);
		jdbcTemplate.update("insert into AppConnection (app, member, accessToken, secret) values (?, ?, ?, ?)",
				appId, accountId, encryptor.encrypt(accessToken), encryptor.encrypt(secret));
		return new AppConnection(accountId, apiKey, accessToken, secret);
	}

	public AppConnection findAppConnection(String accessToken) throws InvalidAccessTokenException {
		try {
			return jdbcTemplate.queryForObject("select c.member, a.apiKey, c.accessToken, c.secret from AppConnection c inner join App a on c.app = a.id where c.accessToken = ?",
				new RowMapper<AppConnection>() {
					public AppConnection mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new AppConnection(rs.getLong("member"), encryptor.decrypt(rs.getString("apiKey")),
								encryptor.decrypt(rs.getString("accessToken")), encryptor.decrypt(rs.getString("secret")));
					}
				}, encryptor.encrypt(accessToken));
		} catch (EmptyResultDataAccessException e) {
			throw new InvalidAccessTokenException(accessToken);
		}
	}

	public void disconnectApp(Long accountId, String accessToken) {
		jdbcTemplate.update("delete from AppConnection where accessToken = ? and member = ?", accessToken, accountId);
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

	private static final String INSERT_ACCOUNT_CONNECTION = "insert into AccountConnection (member, provider, accessToken, accountId) values (?, ?, ?, ?)";

	private static final String SELECT_ACCOUNT_CONNECTION_COUNT = "select count(*) from AccountConnection where member = ? and provider = ?";

	private static final String DELETE_ACCOUNT_CONNECTION = "delete from AccountConnection where member = ? and provider = ?";

}