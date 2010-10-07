package com.springsource.greenhouse.members;

public final class ConnectedProfile {
	
	private final String name;
	
	private final String url;

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
