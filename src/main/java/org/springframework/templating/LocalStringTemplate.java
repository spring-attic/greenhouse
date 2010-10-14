package org.springframework.templating;

public class LocalStringTemplate implements StringTemplate {
	
	private StringTemplate template;
	
	public LocalStringTemplate(String template) {
		this.template = new DelegatingStringTemplate(new org.antlr.stringtemplate.StringTemplate(template));
	}

	public void put(String attribute, Object value) {
		template.put(attribute, value);
	}

	public String render() {
		return template.render();
	}
	
}
