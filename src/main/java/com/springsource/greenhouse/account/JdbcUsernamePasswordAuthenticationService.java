package com.springsource.greenhouse.account;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.springsource.greenhouse.utils.EmailUtils;

public class JdbcUsernamePasswordAuthenticationService implements UsernamePasswordAuthenticationService {

	private JdbcTemplate jdbcTemplate;
	
	private final PasswordEncoder passwordEncoder;
	
	@Inject
	public JdbcUsernamePasswordAuthenticationService(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
		this.jdbcTemplate = jdbcTemplate;
		this.passwordEncoder = passwordEncoder;
	}

	public Account authenticate(String username, String password) throws UsernameNotFoundException, InvalidPasswordException {
		String query = EmailUtils.isEmail(username) ? 
				"select id, firstName, lastName, email, username, password from Member where email = ?" : 
				"select id, firstName, lastName, email, username, password from Member where username = ?";
		try {
			return jdbcTemplate.queryForObject(query, passwordProtectedAccountMapper, username).accessAccount(password, passwordEncoder);
		} catch (EmptyResultDataAccessException e) {
			throw new UsernameNotFoundException(username);
		}
	}
	
	private RowMapper<PasswordProtectedAccount> passwordProtectedAccountMapper = new RowMapper<PasswordProtectedAccount>() {
		
		private RowMapper<Account> accountMapper = new AccountMapper();	

		public PasswordProtectedAccount mapRow(ResultSet rs, int row) throws SQLException {
			Account account = accountMapper.mapRow(rs, row);
			return new PasswordProtectedAccount(account, rs.getString("password"));
		}
	};
	
	private static class PasswordProtectedAccount {
		
		private Account account;
		
		private String password;

		public PasswordProtectedAccount(Account account, String password) {
			this.account = account;
			this.password = password;
		}
	
		public Account accessAccount(String password, PasswordEncoder passwordEncoder) throws InvalidPasswordException {
			if (passwordEncoder.isPasswordValid(this.password, password, account.getId())) {
				return account;
			} else {
				throw new InvalidPasswordException();
			}
		}
		
	}

}
