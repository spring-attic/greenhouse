package com.springsource.greenhouse.develop;

import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.springsource.greenhouse.utils.SlugUtils;

@Repository
public class JdbcConnectedAppRepository implements ConnectedAppRepository {

	private JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcConnectedAppRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<ConnectedAppSummary> findConnectedApps(Long accountId) {
		return jdbcTemplate.query(SELECT_CONNECTED_APPS, connectedAppSummaryMapper, accountId);
	}

	public ConnectedApp findConnectedApp(Long accountId, String slug) {
		return jdbcTemplate.queryForObject(SELECT_CONNECTED_APP, connectedAppMapper, accountId, slug);
	}

	public String updateConnectedApp(Long accountId, String slug, ConnectedAppForm form) {
		String newSlug = createSlug(form.getName());
		jdbcTemplate.update(UPDATE_CONNECTED_APP, connectedAppMapper, accountId, slug);
		return newSlug;
	}

	public void deleteConnectedApp(Long accountId, String slug) {
		jdbcTemplate.update(DELETE_CONNECTED_APP, accountId, slug);
	}

	public ConnectedAppForm getNewAppForm() {
		return new ConnectedAppForm();
	}

	public ConnectedAppForm getAppForm(Long accountId, String slug) {
		return jdbcTemplate.queryForObject(SELECT_CONNECTED_APP_FORM, connectedAppFormMapper, accountId, slug);
	}

	public String createConnectedApp(ConnectedAppForm form) {
		String slug = createSlug(form.getName());
		jdbcTemplate.update(INSERT_CONNECTED_APP, connectedAppFormMapper, slug);		
		return null;
	}

	private String createSlug(String appName) {
		return SlugUtils.toSlug(appName);
	}
	
	private static final String SELECT_CONNECTED_APPS = "select * from ConnectedApp where member = ?";

	private static final String SELECT_CONNECTED_APP = "select * from ConnectedApp where member = ? and slug = ?";

	private static final String SELECT_CONNECTED_APP_FORM = "select * from ConnectedApp where member = ? and slug = ?";

	private static final String UPDATE_CONNECTED_APP = "update ConnectedApp set foo = ? where member = ? and slug = ?";

	private static final String DELETE_CONNECTED_APP = "delete from ConnectedApp where member = ? and slug = ?";

	private static final String INSERT_CONNECTED_APP = "insert into ConnectedApp (foo) values (?)";

	private RowMapper<ConnectedAppSummary> connectedAppSummaryMapper;

	private RowMapper<ConnectedApp> connectedAppMapper;

	private RowMapper<ConnectedAppForm> connectedAppFormMapper;

}
