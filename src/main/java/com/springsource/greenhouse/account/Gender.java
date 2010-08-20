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

	public static Gender valueOf(char charAt) {
		if (charAt == 'M') {
			return Gender.Male;
		} else {
			return Gender.Female;
		}
	}
}
