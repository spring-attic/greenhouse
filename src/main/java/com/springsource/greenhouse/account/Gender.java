package com.springsource.greenhouse.account;

public enum Gender {
	
	Male ('M'), Female ('F');
	
	private char code;
	
	private Gender(char code) { 
		this.code = code;
	}
	
	public char code() {
		return code;
	}
}
