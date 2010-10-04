package com.springsource.greenhouse.utils;

public class ResourceReference<T> {
	
	private final T id;
	
	private final String label;

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
