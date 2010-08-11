package com.springsource.greenhouse.members;

public class ConnectedProfile {
	
	private String name;
	
	private String url;

	public ConnectedProfile(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}
	
}
