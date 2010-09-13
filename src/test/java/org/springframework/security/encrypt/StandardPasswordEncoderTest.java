package org.springframework.security.encrypt;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StandardPasswordEncoderTest {

	StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder("secret");

	@Test
	public void encodePassword() {
		String encodedPassword = passwordEncoder.encode("melbourne");
 		assertTrue(passwordEncoder.matches("melbourne", encodedPassword));
	}
		
}
