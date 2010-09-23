package com.springsource.greenhouse.connect;

import org.springframework.security.encrypt.SearchableStringEncryptor;
import org.springframework.security.encrypt.StringEncryptor;

public class EncryptedStringGenerator {
	public static void main(String[] args) {
		StringEncryptor encryptor = new SearchableStringEncryptor("<3S1unk33!", "b88633de72d173a0");
		String twitterApiKey = "Wy3C6Fef9UQ87ITL9zfg";
		String twitterSecret = "P0laKKNmL3aNcA1orxKI8NvHEtR1EVJpmr9ibF44Y";
		System.out.println(encryptor.encrypt(twitterApiKey));
		System.out.println(encryptor.encrypt(twitterSecret));
		
		String facebookApiKey = "8f007e7ce33d82dc2f5485102b3504c2";
		String facebookSecret = "945d3bc446eca549a70366d00ff73402";
		System.out.println(encryptor.encrypt(facebookApiKey));
		System.out.println(encryptor.encrypt(facebookSecret));

	}
}
