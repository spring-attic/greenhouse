package org.springframework.model;

public class FieldModel<T> {

	private String errorMessage;
	
	private T value;
	
	public FieldModel() {
		
	}
	
	public FieldModel(String errorMessage, T value) {
		this.errorMessage = errorMessage;
		this.value = value;
	}
	
	public boolean isError() {
		return errorMessage != null;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public T getValue() {
		return value;
	}

	public static <T> FieldModel<T> error(String errorMessage, T value) {
		return new FieldModel<T>(errorMessage, value);
	}

}
