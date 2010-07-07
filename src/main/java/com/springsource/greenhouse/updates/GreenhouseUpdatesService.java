package com.springsource.greenhouse.updates;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.springsource.greenhouse.signup.SignupMessage;

public class GreenhouseUpdatesService {

	private JdbcTemplate jdbcTemplate;

	@Inject
	public GreenhouseUpdatesService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void insertSignupUpdate(SignupMessage message) {
		String text = message.getFullName() + " signed up";
		jdbcTemplate.update("insert into update (text, updateTimestamp, userId) values (?, ?, ?)", text, System.currentTimeMillis(), message.getUserId());
	}
	
	public List<Update> getUpdates() {
		return jdbcTemplate.query("select text, updateTimestamp, userId from Update order by updateTimestamp desc", updateMapper);
	}

	private RowMapper<Update> updateMapper = new RowMapper<Update>() {
		public Update mapRow(ResultSet rs, int row) throws SQLException {
			Update update = new Update();
			update.setText(rs.getString("text"));
			update.setTimestamp(rs.getLong("updateTimestamp"));
			update.setUserId(rs.getLong("userId"));
			return update;
		}
	};
}