package com.springsource.greenhouse.events;

public class LeaderFields {

	private Long id;
	
	private String name;
	
	private String company;
	
	private String companyTitle;
	
	private String companyURL;
	
	private String twitterName;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCompany() {
		return company;
	}
	
	public void setCompany(String company) {
		this.company = company;
	}
	
	public String getCompanyTitle() {
		return companyTitle;
	}

	public void setCompanyTitle(String companyTitle) {
		this.companyTitle = companyTitle;
	}
	
	public String getCompanyURL() {
		return companyURL;
	}
	
	public void setCompanyURL(String companyURL) {
		this.companyURL = companyURL;
	}
	
	public String getTwitterName() {
		return twitterName;
	}
	
	public void setTwitterName(String twitterName) {
		this.twitterName = twitterName;
	}

}