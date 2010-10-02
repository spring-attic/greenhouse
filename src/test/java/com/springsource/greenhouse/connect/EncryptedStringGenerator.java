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

		String linkedInApiKey = "nHN6KGWzfCoZktbGo2pwszK1jAvBjoGpWO-K2ZUFnaAGG95mvybkVlhqGeAF0mdG";
		String linkedInSecret = "iQFU_2C1I5nvXkVmZpfSTDITpOnHdert1M7q0NGjgvHsb6_l9xuPoXCDMpuBeDmA";
		System.out.println(encryptor.encrypt(linkedInApiKey));
		System.out.println(encryptor.encrypt(linkedInSecret));

		String tripItApiKey = "91ef8572c06f8ba8bdb7a4b112d872fa775f9ead";
		String tripItSecret = "04c5b2eac08531cc238008fbb61e5de09eb6d906";
		System.out.println(encryptor.encrypt(tripItApiKey));
		System.out.println(encryptor.encrypt(tripItSecret));
		
		String iphoneAppApiKey = "786d1ba0d9f044f6";
		String iphoneAppSecret = "0db3516827a4a925";
		System.out.println(encryptor.encrypt(iphoneAppApiKey));
		System.out.println(encryptor.encrypt(iphoneAppSecret));
	}
}
