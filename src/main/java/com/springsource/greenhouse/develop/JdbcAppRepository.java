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

import com.springsource.greenhouse.utils.SlugUtils;

@Repository
public class JdbcAppRepository implements AppRepository {

	private JdbcTemplate jdbcTemplate;
	
	private StringEncryptor encryptor;
	
	private SecureRandomStringKeyGenerator keyGenerator = new SecureRandomStringKeyGenerator("SHA1PRNG");

	@Inject
	public JdbcAppRepository(JdbcTemplate jdbcTemplate, StringEncryptor encryptor) {
		this.jdbcTemplate = jdbcTemplate;
		this.encryptor = encryptor;
	}

	public List<AppSummary> findAppSummaries(Long accountId) {
		return jdbcTemplate.query(SELECT_APPS, appSummaryMapper, accountId);
	}

	public App findApp(Long accountId, String slug) {
		return jdbcTemplate.queryForObject(SELECT_APP, appMapper, accountId, slug);
	}

	public String updateApp(Long accountId, String slug, AppForm form) {
		String newSlug = createSlug(form.getName());
		jdbcTemplate.update(UPDATE_APP_FORM, form.getName(), form.getDescription(), form.getOrganization(), form.getWebsite(), form.getCallbackUrl(), newSlug, accountId, slug);
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

	public String createApp(Long accountId, AppForm form) {
		String slug = createSlug(form.getName());
		String encryptedConsumerKey = encryptor.encrypt(keyGenerator.generateKey());
		String encryptedSecret = encryptor.encrypt(keyGenerator.generateKey());
		jdbcTemplate.update(INSERT_APP, form.getName(), form.getDescription(), form.getOrganization(), form.getWebsite(), form.getCallbackUrl(), encryptedConsumerKey, encryptedSecret, slug, accountId);		
		return slug;
	}

	private String createSlug(String appName) {
		return SlugUtils.toSlug(appName);
	}
	
	private static final String SELECT_APPS = "select name, description, slug from App where owner = ?";

	private static final String SELECT_APP = "select name, description, slug, consumerKey, secret, callbackUrl from App where owner = ? and slug = ?";

	private static final String SELECT_APP_FORM = "select name, description, organization, website, callbackUrl from App where owner = ? and slug = ?";

	private static final String UPDATE_APP_FORM = "update App set name = ?, description = ?, organization = ?, website = ?, callbackUrl = ?, slug = ? where owner = ? and slug = ?";

	private static final String DELETE_APP = "delete from App where owner = ? and slug = ?";

	private static final String INSERT_APP = "insert into App (name, description, organization, website, callbackUrl, consumerKey, secret, slug, owner) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private RowMapper<AppSummary> appSummaryMapper = new RowMapper<AppSummary>() {
		public AppSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
			String iconUrl = "http://images.greenhouse.springsource.org/default-app-icon.jpg";
			return new AppSummary(rs.getString("name"), iconUrl, rs.getString("description"), rs.getString("slug"));
		}
	};

	private RowMapper<App> appMapper = new RowMapper<App>() {
		public App mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new App(appSummaryMapper.mapRow(rs, rowNum), encryptor.decrypt(rs.getString("consumerKey")), encryptor.decrypt(rs.getString("secret")), rs.getString("callbackUrl"));
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
