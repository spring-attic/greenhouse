package com.springsource.greenhouse.develop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.encrypt.SecureRandomStringKeyGenerator;
import org.springframework.security.encrypt.StringEncryptor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springsource.greenhouse.utils.SlugUtils;

@Repository
public class JdbcAppRepository implements AppRepository {

	private JdbcTemplate jdbcTemplate;
	
	private StringEncryptor encryptor;
	
	private SecureRandomStringKeyGenerator keyGenerator;

	@Inject
	public JdbcAppRepository(JdbcTemplate jdbcTemplate, StringEncryptor encryptor) {
		this.jdbcTemplate = jdbcTemplate;
		this.encryptor = encryptor;
		this.keyGenerator = new SecureRandomStringKeyGenerator();
	}

	public List<AppSummary> findAppSummaries(Long accountId) {
		return jdbcTemplate.query(SELECT_APPS, appSummaryMapper, accountId);
	}

	public App findApp(Long accountId, String slug) {
		return jdbcTemplate.queryForObject(SELECT_APP, appMapper, accountId, slug);
	}

	public String updateApp(Long accountId, String slug, AppForm form) {
		String newSlug = createSlug(form.getName());
		jdbcTemplate.update(UPDATE_APP_FORM, form.getName(), newSlug, form.getDescription(), form.getOrganization(), form.getWebsite(), form.getCallbackUrl(), accountId, slug);
		return newSlug;
	}

	public void deleteApp(Long accountId, String slug) {
		jdbcTemplate.update(DELETE_APP, accountId, slug);
	}

	public AppForm getNewAppForm() {
		return new AppForm();
	}

	public AppForm getAppForm(Long accountId, String slug) {
		return jdbcTemplate.queryForObject(SELECT_APP_FORM, appFormMapper, accountId, slug);
	}

	@Transactional
	public String createApp(Long accountId, AppForm form) {
		String slug = createSlug(form.getName());
		String encryptedApiKey = encryptor.encrypt(keyGenerator.generateKey());
		String encryptedSecret = encryptor.encrypt(keyGenerator.generateKey());
		jdbcTemplate.update(INSERT_APP, form.getName(), slug, form.getDescription(), form.getOrganization(), form.getWebsite(), encryptedApiKey, encryptedSecret, form.getCallbackUrl());
		Long appId = jdbcTemplate.queryForLong("call identity()");
		jdbcTemplate.update(INSERT_APP_DEVELOPER, appId, accountId);
		return slug;
	}

	private String createSlug(String appName) {
		return SlugUtils.toSlug(appName);
	}
	
	private static final String SELECT_APPS = "select a.name, a.slug, a.description from App a inner join AppDeveloper d on a.id = d.app where d.member = ?";

	private static final String SELECT_APP = "select a.name, a.slug, a.description, a.apiKey, a.secret, a.callbackUrl from App a inner join AppDeveloper d on a.id = d.app where d.member = ? and a.slug = ?";

	private static final String SELECT_APP_FORM = "select a.name, a.description, a.organization, a.website, a.callbackUrl from App a inner join AppDeveloper d on a.id = d.app where d.member = ? and a.slug = ?";

	private static final String UPDATE_APP_FORM = "update App set name = ?, slug = ?, description = ?, organization = ?, website = ?, callbackUrl = ? where exists(select 1 from AppDeveloper where member = ?) and slug = ?";

	private static final String DELETE_APP = "delete from App where exists(select 1 from AppDeveloper where member = ?) and slug = ?";

	private static final String INSERT_APP = "insert into App (name, slug, description, organization, website, apiKey, secret, callbackUrl) values (?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String INSERT_APP_DEVELOPER = "insert into AppDeveloper (app, member) values (?, ?)";

	private RowMapper<AppSummary> appSummaryMapper = new RowMapper<AppSummary>() {
		public AppSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
			String iconUrl = "http://images.greenhouse.springsource.org/default-app-icon.jpg";
			return new AppSummary(rs.getString("name"), iconUrl, rs.getString("description"), rs.getString("slug"));
		}
	};

	private RowMapper<App> appMapper = new RowMapper<App>() {
		public App mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new App(appSummaryMapper.mapRow(rs, rowNum), encryptor.decrypt(rs.getString("apiKey")), encryptor.decrypt(rs.getString("secret")), rs.getString("callbackUrl"));
		}
	};

	private RowMapper<AppForm> appFormMapper = new RowMapper<AppForm>() {
		public AppForm mapRow(ResultSet rs, int rowNum) throws SQLException {
			AppForm form = new AppForm();
			form.setName(rs.getString("name"));
			form.setDescription(rs.getString("description"));
			form.setOrganization(rs.getString("organization"));
			form.setWebsite(rs.getString("website"));
			form.setCallbackUrl(rs.getString("callbackUrl"));
			return form;
		}
	};

}
