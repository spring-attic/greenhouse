package org.springframework.templating;

public interface StringTemplate {
	
	void put(String attribute, Object value);
	
	String render();
		
}