package com.springsource.greenhouse.reset;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.account.Account;
import com.springsource.greenhouse.account.AccountNotFoundException;
import com.springsource.greenhouse.account.AccountRepository;

@Service
@Transactional
public class DefaultRestPasswordService implements ResetPasswordService {

	private JdbcTemplate jdbcTemplate;

	private AccountRepository accountRepository;
	
	private ResetPasswordMailer mailer;
	
	@Inject
	public DefaultRestPasswordService(JdbcTemplate jdbcTemplate, AccountRepository accountRepository, ResetPasswordMailer mailer) {
		this.jdbcTemplate = jdbcTemplate;
		this.accountRepository = accountRepository;
	}

	public void sendResetMail(String username) throws AccountNotFoundException {
		Account account = accountRepository.findAccount(username);
		String token = UUID.randomUUID().toString();
 		jdbcTemplate.update("insert into ResetPassword (token, member) values (?, ?)", token, account.getId());
 		mailer.send(new ResetPasswordRequest(token, account));
	}

	public boolean isValidResetToken(String token) {
		return jdbcTemplate.queryForInt("select count(*) from ResetPassword where token = ?", token) == 1;
	}

	public void changePassword(String token, String password) throws InvalidTokenException {
		Long memberId = findByToken(token);
		jdbcTemplate.update("update Member set password = ? where id = ?", password, memberId);
		jdbcTemplate.update("delete from ResetPassword where token = ?", token);
	}

	// internal helpers
	
	private Long findByToken(String token) throws InvalidTokenException {
		try {
			return jdbcTemplate.queryForLong("select member from ResetPassword where token = ?", token);       		
		} catch (EmptyResultDataAccessException e) {
			throw new InvalidTokenException(token);
		}
	}

}