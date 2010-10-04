package com.springsource.greenhouse.utils;

public final class SubResourceReference<P, C> extends ResourceReference<C> {

	private final P parentId;
	
	public SubResourceReference(P parentId, C id, String label) {
		super(id, label);
		this.parentId = parentId;
	}

	public P getParentId() {
		return parentId;
	}

}
