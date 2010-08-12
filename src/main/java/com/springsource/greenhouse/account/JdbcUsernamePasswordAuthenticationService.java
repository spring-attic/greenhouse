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
			Authenticate authenticate = jdbcTemplate.queryForObject(query, authenticateMapper, username);
			if (!authenticate.passwordMatches(passwordEncoder, password)) {
				throw new InvalidPasswordException();
			}
			return authenticate.getAccount();
		} catch (EmptyResultDataAccessException e) {
			throw new UsernameNotFoundException(username);
		}
	}
	
	private RowMapper<Authenticate> authenticateMapper = new RowMapper<Authenticate>() {
		
		private RowMapper<Account> accountMapper = new AccountMapper();	

		public Authenticate mapRow(ResultSet rs, int row) throws SQLException {
			Account account = accountMapper.mapRow(rs, row);
			return new Authenticate(account, rs.getString("password"));
		}
	};
	
	private static class Authenticate {
		
		private Account account;
		
		private String password;

		public Authenticate(Account account, String password) {
			this.account = account;
			this.password = password;
		}
	
		public boolean passwordMatches(PasswordEncoder passwordEncoder, String password) {
			return passwordEncoder.isPasswordValid(this.password, password, account.getId());
		}

		public Account getAccount() {
			return account;
		}
	}

}
