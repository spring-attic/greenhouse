package org.springframework.templating;

//Copied verbatim from Spring WebFlow - See http://jira.springframework.org/browse/SPR-6987
public interface StringTemplate {
	
	void put(String attribute, Object value);
	
	String render();
		
}