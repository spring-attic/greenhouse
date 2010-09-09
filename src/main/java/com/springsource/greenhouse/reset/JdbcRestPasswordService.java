package com.springsource.greenhouse.reset;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountRepository;
import com.springsource.greenhouse.account.UsernameNotFoundException;

@Service
public class JdbcRestPasswordService implements ResetPasswordService {

	private JdbcTemplate jdbcTemplate;

	private AccountRepository accountRepository;
	
	private ResetPasswordMailer mailer;
	
	@Inject
	public JdbcRestPasswordService(JdbcTemplate jdbcTemplate, AccountRepository accountRepository, ResetPasswordMailer mailer) {
		this.jdbcTemplate = jdbcTemplate;
		this.accountRepository = accountRepository;
		this.mailer = mailer;
	}

	@Transactional
	public void sendResetMail(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByUsername(username);
		String token = UUID.randomUUID().toString();
 		jdbcTemplate.update("insert into ResetPassword (token, member) values (?, ?)", token, account.getId());
 		mailer.send(new ResetPasswordRequest(token, account));
	}

	public boolean isValidResetToken(String token) {
		return jdbcTemplate.queryForInt("select count(*) from ResetPassword where token = ?", token) == 1;
	}

	@Transactional
	public void changePassword(String token, String password) throws InvalidResetTokenException {
		Long accountId = findAccountIdByToken(token);
		accountRepository.changePassword(accountId, password);
		jdbcTemplate.update("delete from ResetPassword where token = ?", password);
	}

	// internal helpers
	
	private Long findAccountIdByToken(String token) throws InvalidResetTokenException {
		try {
			return jdbcTemplate.queryForLong("select member from ResetPassword where token = ?", token);       		
		} catch (EmptyResultDataAccessException e) {
			throw new InvalidResetTokenException(token);
		}
	}

}