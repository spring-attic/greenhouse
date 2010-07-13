package com.springsource.greenhouse.updates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.springsource.greenhouse.account.Account;

public class GreenhouseUpdatesService {

	private JdbcTemplate jdbcTemplate;

	@Inject
	public GreenhouseUpdatesService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void insertSignupUpdate(Account account) {
		String text = account.getFullName() + " signed up";
		jdbcTemplate.update("insert into update (text, updateTimestamp, member) values (?, ?, ?)", text, System.currentTimeMillis(), account.getId());
	}
	
	public List<Update> getUpdates() {
		return jdbcTemplate.query("select text, updateTimestamp, member from Update order by updateTimestamp desc", updateMapper);
	}

	private RowMapper<Update> updateMapper = new RowMapper<Update>() {
		public Update mapRow(ResultSet rs, int row) throws SQLException {
			Update update = new Update();
			update.setText(rs.getString("text"));
			update.setTimestamp(rs.getLong("updateTimestamp"));
			update.setUserId(rs.getLong("member"));
			return update;
		}
	};
}