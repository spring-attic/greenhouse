package org.springframework.templating;

class DelegatingStringTemplate implements StringTemplate {

	private org.antlr.stringtemplate.StringTemplate delegate;

	public DelegatingStringTemplate(org.antlr.stringtemplate.StringTemplate delegate) {
		this.delegate = delegate;
	}

	public void put(String name, Object value) {
		delegate.setAttribute(name, value);
	}

	public String render() {
		return delegate.toString();
	}

}