package com.springsource.greenhouse.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class EmailUtilsTest {
  @Test
  public void shouldRecognizeAnEmail() {
      assertTrue(EmailUtils.isEmail("test@test.com"));
  }
  
  @Test
  public void shouldRecognizeANonEmail() {
      assertFalse(EmailUtils.isEmail("chuck"));
      assertFalse(EmailUtils.isEmail("@"));
      assertFalse(EmailUtils.isEmail("a@b.c"));
      assertFalse(EmailUtils.isEmail("a@b.chuck"));
      assertFalse(EmailUtils.isEmail("a@b.cd1"));
  }
}
