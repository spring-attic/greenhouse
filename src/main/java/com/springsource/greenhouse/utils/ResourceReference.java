package com.springsource.greenhouse.utils;

public class ResourceReference<T> {
	
	private T id;
	
	private String label;

	public ResourceReference(T id, String label) {
		this.id = id;
		this.label = label;
	}

	public T getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}
	
}
