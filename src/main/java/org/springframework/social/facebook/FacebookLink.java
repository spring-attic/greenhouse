package org.springframework.social.facebook;

public class FacebookLink {
	private final String link;
	private final String name;
	private final String caption;
	private final String description;

	public FacebookLink(String link, String name, String caption, String description) {
		this.link = link;
		this.name = name;
		this.caption = caption;
		this.description = description;	
	}

	public String getLink() {
    	return link;
    }

	public String getName() {
    	return name;
    }

	public String getCaption() {
    	return caption;
    }

	public String getDescription() {
    	return description;
    }
}
