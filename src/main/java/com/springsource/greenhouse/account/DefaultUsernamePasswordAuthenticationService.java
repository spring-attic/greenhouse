package com.springsource.greenhouse.account;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.springsource.greenhouse.utils.EmailUtils;

public class DefaultUsernamePasswordAuthenticationService implements UsernamePasswordAuthenticationService {

	private JdbcTemplate jdbcTemplate;
	
	@Inject
	public DefaultUsernamePasswordAuthenticationService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Account authenticate(String username, String password) throws AccountNotFoundException, InvalidPasswordException {
		String query = EmailUtils.isEmail(username) ? 
				"select id, firstName, lastName, email, username, password from Member where email = ?" : 
				"select id, firstName, lastName, email, username, password from Member where username = ?";
		try {
			Authenticate authenticate = jdbcTemplate.queryForObject(query, authenticateMapper, username);
			if (!authenticate.passwordMatches(password)) {
				throw new InvalidPasswordException();
			}
			return authenticate.getAccount();
		} catch (EmptyResultDataAccessException e) {
			throw new AccountNotFoundException(username);
		}
	}
	
	private RowMapper<Authenticate> authenticateMapper = new RowMapper<Authenticate>() {
		
		private RowMapper<Account> accountMapper = new AccountMapper();	

		public Authenticate mapRow(ResultSet rs, int row) throws SQLException {
			Account account = accountMapper.mapRow(rs, row);
			String decryptedPassword = decrypt(rs.getString("password"));
			return new Authenticate(account, decryptedPassword);
		}
		
		private String decrypt(String password) {
			// TODO decrypt password
			return password;
		}
		
	};
	
	private static class Authenticate {
		
		private Account account;
		
		private String password;

		public Authenticate(Account account, String password) {
			this.account = account;
			this.password = password;
		}
	
		public boolean passwordMatches(String password) {
			return this.password.equals(password);
		}

		public Account getAccount() {
			return account;
		}
	}

}
